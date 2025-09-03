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

    // 双爆逻辑(玩家触发)
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) throws IOException {
        ItemConfig.init();
        if (!ItemConfig.getCustomC2C()) return; // 检测是否开启自定义双爆
        
        Player player = event.getEntity();
        Entity target = event.getTarget();
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
        }
    }
}
