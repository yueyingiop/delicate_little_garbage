package com.core.DLG.events;

import java.io.IOException;

import com.core.DLG.DLG;
import com.core.DLG.configs.EnchantmentConfig;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArrowNockEventHandler {

    // 使无限附魔不需要箭
    @SubscribeEvent
    public static void trulyInfinite(ArrowNockEvent event) throws IOException {
        EnchantmentConfig.init();
        if (event.getBow().getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0 && EnchantmentConfig.getTrulyInfinite()) {
            event.getEntity().startUsingItem(event.getHand());
            event.setAction(InteractionResultHolder.success(event.getBow()));
        }
    }
}
