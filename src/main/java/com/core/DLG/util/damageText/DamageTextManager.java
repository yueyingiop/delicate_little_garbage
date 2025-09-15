package com.core.DLG.util.damageText;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageTextManager {
    private static final List<DamageTextRenderer> textRenderers = new ArrayList<>();

    public static void addTextRenderer(DamageTextRenderer textRenderer) {
        textRenderers.add(textRenderer);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase == ClientTickEvent.Phase.END) {
            textRenderers.removeIf(DamageTextRenderer::isExpired);
            textRenderers.forEach(DamageTextRenderer::tick);
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            PoseStack poseStack = event.getPoseStack();
            Camera camera = event.getCamera();
            textRenderers.forEach(renderer -> renderer.render(poseStack, camera, event.getPartialTick()));
        }
    }

    public static void spawnDamageText(Level level, LivingEntity entity, double damage, int damageTypeId) {
        if (level.isClientSide) {
            addTextRenderer(new DamageTextRenderer(
                (net.minecraft.client.multiplayer.ClientLevel) level,
                entity, damage, damageTypeId
            ));
        }
    }
}
