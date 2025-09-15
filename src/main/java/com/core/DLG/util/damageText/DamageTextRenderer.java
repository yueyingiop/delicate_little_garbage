package com.core.DLG.util.damageText;

import java.util.Random;

import com.core.DLG.enums.DamageTypeEnum;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DamageTextRenderer {
    private static final Random RANDOM = new Random();
    private final LivingEntity entity;
    private final Component damageString;
    private final DamageTypeEnum damageType;
    private int lifetime;
    private int age;
    private double offsetX;
    private double offsetZ;
    private float scale;

    public DamageTextRenderer(
        ClientLevel clientLevel, 
        LivingEntity entity,
        double damageAmount, 
        int damageType
    ) {
        this.entity = entity;
        this.lifetime = 25 + clientLevel.random.nextInt(5);
        String text = String.format("%.1f", damageAmount);
        this.damageString = Component.literal(text);
        this.damageType = DamageTypeEnum.getById(damageType);
        this.scale = 1.0F;
        this.offsetX = (RANDOM.nextFloat() * 0.5 + 0.5) * (RANDOM.nextBoolean() ? -1.0 : 1.0);
        this.offsetZ = (RANDOM.nextFloat() * 0.5 + 0.5) * (RANDOM.nextBoolean() ? -1.0 : 1.0);
    }

    public void tick() {
        this.age++;
        float ageScaled = age / (float) lifetime;
        this.scale = 1.0F - ageScaled;
    }

    public void render(PoseStack poseStack, Camera camera, float partialTicks) {
        float currentAge = age + partialTicks; // 当前年龄
        if (currentAge > lifetime) return;

        double renderX = Mth.lerp(partialTicks, entity.xOld, entity.getX());
        double renderY = Mth.lerp(partialTicks, entity.yOld, entity.getY()) + entity.getBbHeight();
        double renderZ = Mth.lerp(partialTicks, entity.zOld, entity.getZ());

        double offsetY = currentAge * 0.05;
        renderX += offsetX;
        renderY += offsetY;
        renderZ += offsetZ;

        float ageFactor = currentAge / (float) lifetime; // 年龄因子
        float alpha = 1.0F - ageFactor; // 透明度
        float scale = this.scale; // 缩放

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        int color = damageType.getColor(); // 文本颜色
        int colorOutline = damageType.getColorOutline();
        int alphaComponent = (int) (alpha * 255.0F) << 24; // 透明度
        color = (color & 0xFFFFFF) | alphaComponent; // 添加透明度

        poseStack.pushPose();

        Vec3 cameraPos = camera.getPosition();
        double camX = cameraPos.x;
        double camY = cameraPos.y;
        double camZ = cameraPos.z;
        poseStack.translate(renderX - camX, renderY - camY, renderZ - camZ);
        
        poseStack.mulPose(camera.rotation());

        float textScale = 0.025F * scale; // 文本缩放
        poseStack.scale(-textScale, -textScale, -textScale);

        float textWidth = font.width(this.damageString) / 2.0F;

        BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        font.drawInBatch8xOutline(
            this.damageString.getVisualOrderText(), 
            -textWidth, 
            0, 
            color, 
            colorOutline, 
            poseStack.last().pose(), 
            bufferSource, 
            15728880
        );

        bufferSource.endBatch();
        poseStack.popPose();
    }

    public boolean isExpired() {
        return age >= lifetime;
    }
}
