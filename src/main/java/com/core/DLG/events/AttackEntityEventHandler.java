package com.core.DLG.events;

import java.io.IOException;
import java.util.Random;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttackEntityEventHandler {
    private static final Random RANDOM = new Random();

    // 自定义属性逻辑(玩家触发)
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) throws IOException {
        ItemConfig.init();
        if (!ItemConfig.getCustomAttribute()) return; // 检测是否开启自定义双爆
        
        Player player = event.getEntity(); // 攻击方
        Entity target = event.getTarget(); // 被攻击方
        if (!player.level().isClientSide && target instanceof LivingEntity) {
            double criticalChance = player.getAttributeValue(RegistryAttribute.CRITICAL_CHANCE.get());
            double criticalDamageMultiplier = 1 + player.getAttributeValue(RegistryAttribute.CRITICAL_DAMAGE.get());
            // 触发暴击
            if (RANDOM.nextDouble() < criticalChance) {
                player.getPersistentData().putBoolean("DLG_CriticalHit", true);
                player.getPersistentData().putDouble("DLG_CriticalMultiplier", criticalDamageMultiplier);
            } else {
                player.getPersistentData().remove("DLG_CriticalHit");
                player.getPersistentData().remove("DLG_CriticalMultiplier");
            }

            double penetrationChance = player.getAttributeValue(RegistryAttribute.PENETRATION_CHANCE.get());
            double penetrationDamageMultiplier = player.getAttributeValue(RegistryAttribute.PENETRATION_DAMAGE.get());
            // 触发穿透
            if (RANDOM.nextDouble() < penetrationChance) {
                player.getPersistentData().putBoolean("DLG_PenetrationHit", true);
                player.getPersistentData().putDouble("DLG_PenetrationMultiplier", penetrationDamageMultiplier);
            } else {
                player.getPersistentData().remove("DLG_PenetrationHit");
                player.getPersistentData().remove("DLG_PenetrationMultiplier");
            }

            double lifestealChance = player.getAttributeValue(RegistryAttribute.LIFESTEAL_CHANCE.get());
            double lifestealDamageMultiplier = player.getAttributeValue(RegistryAttribute.LIFESTEAL_DAMAGE.get());
            // 触发吸血
            if (RANDOM.nextDouble() < lifestealChance) {
                player.getPersistentData().putBoolean("DLG_LifestealHit", true);
                player.getPersistentData().putDouble("DLG_LifestealMultiplier", lifestealDamageMultiplier);
            } else {
                player.getPersistentData().remove("DLG_LifestealHit");
                player.getPersistentData().remove("DLG_LifestealMultiplier");
            }
        }
    }
}
