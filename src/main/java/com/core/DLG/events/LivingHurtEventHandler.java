package com.core.DLG.events;

import com.core.DLG.DLG;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandler {
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        
        // 检查伤害来源是否是玩家
        if (source.getEntity() instanceof Player player) {
            // 检查是否有暴击标记
            if (player.getPersistentData().contains("DLG_CriticalHit") && player.getPersistentData().getBoolean("DLG_CriticalHit")) {
                
                // 获取暴击倍率
                double multiplier = player.getPersistentData().getDouble("DLG_CriticalMultiplier");
                
                // 应用暴击伤害
                float originalDamage = event.getAmount();
                float criticalDamage = (float) (originalDamage * multiplier);
                event.setAmount(criticalDamage);
                
                // 清除暴击标记
                player.getPersistentData().remove("DLG_CriticalHit");
                player.getPersistentData().remove("DLG_CriticalMultiplier");

                player.displayClientMessage(
                    Component.translatable("暴击成功"+String.valueOf(multiplier)+"-"+String.valueOf(criticalDamage)+",伤害:"+String.valueOf(criticalDamage)).withStyle(ChatFormatting.YELLOW), 
                    true
                );
            }
        }
    }
}
