package com.core.DLG.util.damageHUD;

import com.core.DLG.configs.PlayerConfig;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class DamageHUDRenderer {
    private String damageText;
    private long lastDamageTime; // 最后伤害时间（游戏时间）
    private static final int RESET_TIME_TICKS = 100; // 5秒 (20 ticks/秒)

    public DamageHUDRenderer(double damage, long lastDamageTime) {
        updateDamage(damage, lastDamageTime);
    }

    public void updateDamage(double damage, long lastDamageTime) {
        this.damageText = String.format("总伤害: %.1f", damage);
        this.lastDamageTime = lastDamageTime;
    }

    public void renderDamageHUD(GuiGraphics guiGraphics, int width, int height) {
        try {
            PlayerConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // 检查是否在战斗中（使用游戏时间）
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        long currentTime = level.getGameTime();
        boolean inCombat = currentTime - lastDamageTime <= RESET_TIME_TICKS;


        if (inCombat && PlayerConfig.getDamageHUDShow()) {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            
            int textWidth = Minecraft.getInstance().font.width(damageText);
            
            // 在右上角绘制伤害文本
            int x = width - textWidth - 10;
            int y = 10;
            
            // 绘制半透明背景
            guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + 9, 0x80000000);
            
            // 绘制文本
            guiGraphics.drawString(Minecraft.getInstance().font, damageText, x, y, 0xFFFFFF);
            
            poseStack.popPose();
        }
    }
}
