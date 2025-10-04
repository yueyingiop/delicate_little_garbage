package com.core.DLG.events;

import java.io.IOException;
import java.util.Random;
import java.util.WeakHashMap;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.DamageTypeEnum;
import com.core.DLG.util.damageText.DamageTextPacket;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandler {
    private static final Random RANDOM = new Random();

    // 缓存玩家伤害信息
    private static final WeakHashMap<Player, DamageInfo> playerCriticalDamageMap = new WeakHashMap<>();
    private static final WeakHashMap<Player, DamageInfo> playerPenetrationDamageMap = new WeakHashMap<>();
    // 记录玩家伤害信息
    private static class DamageInfo {
        public final float originalDamage; // 基础伤害
        public final float modifiedDamage; // 倍率后伤害
        public final double multiplier; // 倍率
        public final long timestamp;

        public DamageInfo(float originalDamage, float modifiedDamage, double multiplier) {
            this.originalDamage = originalDamage;
            this.modifiedDamage = modifiedDamage;
            this.multiplier = multiplier;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 处理闪避
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void dodgeDeposit(LivingHurtEvent event) {

        if (event.getEntity() instanceof Player player) {
            double dodge = player.getAttributeValue(RegistryAttribute.DODGE.get());
            if (RANDOM.nextDouble() < dodge && event.getSource().getEntity() != null) {
                event.setCanceled(true);
                event.setAmount(0);
                player.displayClientMessage(
                    Component.translatable("tips.dlg.attribute.dodge").withStyle(ChatFormatting.YELLOW), 
                    true
                );
            }
        }
        
    }

    // 处理自定义伤害(基于基础攻击力)
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) throws IOException {
        ItemConfig.init();
        if (!ItemConfig.getCustomAttribute()) return; // 检测是否开启自定义词条
        if (event.isCanceled()) return; // 如果事件已被取消，则不处理

        DamageSource source = event.getSource();
        
        // 检查伤害来源是否是玩家
        if (source.getEntity() instanceof Player player) {
            // 检查是否有暴击标记
            if (
                player.getPersistentData().contains("DLG_CriticalHit") && 
                player.getPersistentData().getBoolean("DLG_CriticalHit")
            ) {
                // 获取暴击倍率
                double multiplier = player.getPersistentData().getDouble("DLG_CriticalMultiplier");
                
                // 应用暴击伤害
                float originalDamage = event.getAmount();
                float criticalDamage = (float) (originalDamage * multiplier);
                playerCriticalDamageMap.put(
                    player,
                    new DamageInfo(originalDamage, criticalDamage, multiplier)
                );
                event.setAmount(criticalDamage);
                
                
                // 清除暴击标记
                player.getPersistentData().remove("DLG_CriticalHit");
                player.getPersistentData().remove("DLG_CriticalMultiplier");
            }

            // 是否有穿透标记
            if (
                player.getPersistentData().contains("DLG_PenetrationHit") && 
                player.getPersistentData().getBoolean("DLG_PenetrationHit")
            ) {
                double multiplier = player.getPersistentData().getDouble("DLG_PenetrationMultiplier");
                float originalDamage = event.getAmount();
                float penetrationDamage = (float) (originalDamage * multiplier);
                playerPenetrationDamageMap.put(
                    player,
                    new DamageInfo(originalDamage, penetrationDamage, multiplier)
                );
                player.getPersistentData().remove("DLG_PenetrationHit");
                player.getPersistentData().remove("DLG_PenetrationMultiplier");
            }
        }
    }

    // 获取最终伤害
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return; // 仅在服务器端处理

        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            DamageInfo criticalDamageInfo = playerCriticalDamageMap.get(player);
            DamageInfo penetrationDamageInfo = playerPenetrationDamageMap.get(player);

            float actualDamage = event.getAmount();
            long currentTime = System.currentTimeMillis();

            if (actualDamage < 0.1f) return; // 伤害过低不显示

            LivingEntity target = event.getEntity();

            DamageTypeEnum damageType = DamageTypeEnum.NORMAL;

            // 伤害触发检测
            if (criticalDamageInfo != null && currentTime - criticalDamageInfo.timestamp < 500) {
                damageType = DamageTypeEnum.CRITICAL;
                playerCriticalDamageMap.remove(player);
            }

            DLG.NETWORK.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), 
                new DamageTextPacket(target.getId(), actualDamage, damageType.getId())
            );

            if (penetrationDamageInfo != null && currentTime - penetrationDamageInfo.timestamp < 500) {
                event.setAmount(actualDamage+penetrationDamageInfo.modifiedDamage);
                DLG.NETWORK.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), 
                    new DamageTextPacket(target.getId(), penetrationDamageInfo.modifiedDamage, DamageTypeEnum.PENETRATION.getId())
                );
                playerPenetrationDamageMap.remove(player);
            }


            // 检测吸血标记
            if (player.getPersistentData().contains("DLG_LifestealHit") && player.getPersistentData().getBoolean("DLG_LifestealHit")) {
                double multiplier = player.getPersistentData().getDouble("DLG_LifestealMultiplier");
                float originalDamage = event.getAmount();
                float lifestealDamage = (float) (originalDamage * multiplier);
                player.getPersistentData().remove("DLG_LifestealHit");
                player.getPersistentData().remove("DLG_LifestealMultiplier");

                if (lifestealDamage < 0.1f) return;
                float currentHealth = player.getHealth();
                float maxHealth = player.getMaxHealth();
                player.setHealth(Math.min(currentHealth + lifestealDamage, maxHealth));
                
                DLG.NETWORK.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), 
                    new DamageTextPacket(target.getId(), lifestealDamage, DamageTypeEnum.LIFESTEAL.getId())
                );
            }
        }
    }
}
