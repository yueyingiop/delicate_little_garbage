package com.core.DLG.util.damageHUD;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageHUDManager {
    private static DamageHUDRenderer damageRenderer = new DamageHUDRenderer(0,0);


    public static void updateDamage(double damage, long lastDamageTime) {
        damageRenderer.updateDamage(damage, lastDamageTime);
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!Minecraft.getInstance().options.renderDebug && Minecraft.getInstance().player != null) {
            damageRenderer.renderDamageHUD(
                event.getGuiGraphics(), 
                event.getWindow().getGuiScaledWidth(), 
                event.getWindow().getGuiScaledHeight()
            );
        }
    }

    public static void spawnDamageHUD(double damage, long lastDamageTime) {
        updateDamage(damage, lastDamageTime);
    }
}
