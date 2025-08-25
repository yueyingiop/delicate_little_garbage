package com.core.DLG.mixin.client.appleskin;

import java.io.IOException;
import java.util.Vector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.core.DLG.configs.PlayerConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import squeek.appleskin.client.HUDOverlayHandler;
import squeek.appleskin.helpers.TextureHelper;
import squeek.appleskin.util.IntPoint;


@Mixin(HUDOverlayHandler.class)
public abstract class HUDOverlayHandlerMixin {
    private static Vector<IntPoint> multiLineFoodBarOffsets = new Vector<>();

    // 重定向Vector.get方法
    @Redirect(method = "drawSaturationOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Vector;get(I)Ljava/lang/Object;"), remap = false)
    private static Object redirectSaturationOffsetGet(Vector<IntPoint> instance, int index) throws IOException {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return null;

        PlayerConfig.init();
        int maxLevel = PlayerConfig.getMaxHungry();
        if (maxLevel >= 1000) maxLevel = 1000;

        int totalIcons = (int) Math.ceil(maxLevel / 2.0);

        if (index > 10) {
            if (multiLineFoodBarOffsets.size() < totalIcons) {
                multiLineFoodBarOffsets.setSize(totalIcons);
            }

            int row = index / 10;
            int col = index % 10;

            IntPoint point = multiLineFoodBarOffsets.get(index);
            if (point == null) {
                point = new IntPoint();
                multiLineFoodBarOffsets.set(index, point);
            }

            int right = mc.getWindow().getGuiScaledWidth() / 2 + 91;
            int top = mc.getWindow().getGuiScaledHeight() - 39;

            int x = right - col * 8 - 9;
            int y = top - row * 9;

            if (row == 0 && player.getFoodData().getSaturationLevel() <= 0.0F && 
                mc.gui.getGuiTicks() % (player.getFoodData().getFoodLevel() * 3 + 1) == 0) {
                y += mc.level.random.nextInt(3) - 1;
            }

            point.x = x - right;
            point.y = y - top;
            
            return point;
        }
        
        return instance.get(index);
    }

    // 重定向Vector.get方法
    @Redirect(method = "drawHungerOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Vector;get(I)Ljava/lang/Object;"), remap = false)
    private static Object redirectHungerOffsetGet(Vector<IntPoint> instance, int index) throws IOException{
        return redirectSaturationOffsetGet(instance, index);
    }

    // 生成多行食物图标的偏移量
    @Inject(method = "generateHungerBarOffsets", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onGenerateHungerBarOffsets(int top, int left, int right, int ticks, Player player, CallbackInfo ci) throws IOException {
        PlayerConfig.init();

        Minecraft mc = Minecraft.getInstance();
        
        // 获取最大饥饿值
        int maxLevel = PlayerConfig.getMaxHungry();
        if (maxLevel >= 1000) maxLevel = 1000;

        // 计算总图标数
        int totalIcons = (int) Math.ceil(maxLevel / 2.0);
        
        // 设置foodBarOffsets的大小为总图标数
        HUDOverlayHandler.foodBarOffsets.setSize(totalIcons);

        for (int i = 0; i < totalIcons; i++) {
            int row = i / 10;
            int col = i % 10;
            
            // 计算X坐标（从右向左）
            int x = right - col * 8 - 9;
            
            // 计算Y坐标（从下向上）
            int y = top - row * 9;
            
            // 应用抖动效果（如果适用）
            boolean shouldAnimatedFood = false;
            if (row == 0) {
                float saturationLevel = player.getFoodData().getSaturationLevel();
                int foodLevel = player.getFoodData().getFoodLevel();
                shouldAnimatedFood = saturationLevel <= 0.0F && ticks % (foodLevel * 3 + 1) == 0;
                
                if (shouldAnimatedFood) {
                    y += mc.level.random.nextInt(3) - 1;
                }
            }
            
            // 获取或创建偏移点
            IntPoint point = HUDOverlayHandler.foodBarOffsets.get(i);
            if (point == null) {
                point = new IntPoint();
                HUDOverlayHandler.foodBarOffsets.set(i, point);
            }
            
            // 设置偏移量（相对于右下角）
            point.x = x - right;
            point.y = y - top;
        }
        ci.cancel();
    }

    // 绘制饱和度图标
    @Inject(method = "drawSaturationOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onDrawSaturationOverlay(float saturationGained, float saturationLevel, Minecraft mc, GuiGraphics guiGraphics, int right, int top, float alpha, CallbackInfo ci) {
        // 获取最大饥饿值
        int maxLevel = PlayerConfig.getMaxHungry();
        if (maxLevel >= 1000) maxLevel = 1000;
        
        if (saturationLevel + saturationGained < 0) {
            ci.cancel();
            return;
        }

        HUDOverlayHandler.enableAlpha(alpha);

        float modifiedSaturation = Math.max(0, Math.min(saturationLevel + saturationGained, maxLevel));

        int startSaturationBar = 0;
        int endSaturationBar = (int) Math.ceil(modifiedSaturation / 2.0F);

        if (saturationGained != 0)
            startSaturationBar = (int) Math.max(saturationLevel / 2.0F, 0);

        int iconSize = 9;

        for (int i = startSaturationBar; i < endSaturationBar; ++i) {
            IntPoint offset = HUDOverlayHandler.foodBarOffsets.get(i);
            if (offset == null)
                continue;

            int x = right + offset.x;
            int y = top + offset.y;

            int v = 0;
            int u = 0;

            float effectiveSaturationOfBar = (modifiedSaturation / 2.0F) - i;

            if (effectiveSaturationOfBar >= 1)
                u = 3 * iconSize;
            else if (effectiveSaturationOfBar > .5)
                u = 2 * iconSize;
            else if (effectiveSaturationOfBar > .25)
                u = 1 * iconSize;

            guiGraphics.blit(TextureHelper.MOD_ICONS, x, y, u, v, iconSize, iconSize);
        }

        RenderSystem.setShaderTexture(0, TextureHelper.MC_ICONS);
        HUDOverlayHandler.disableAlpha(alpha);
        
        ci.cancel();
    }

    // 绘制饥饿图标
    @Inject(method = "drawHungerOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onDrawHungerOverlay(int hungerRestored, int foodLevel, Minecraft mc, GuiGraphics guiGraphics, int right, int top, float alpha, boolean useRottenTextures, CallbackInfo ci) { 
        int maxLevel = PlayerConfig.getMaxHungry();
        if (maxLevel >= 1000) maxLevel = 1000;

        if (hungerRestored <= 0) {
            ci.cancel();
			return;
        }

        HUDOverlayHandler.enableAlpha(alpha);

        int modifiedFood = Math.max(0, Math.min(maxLevel, foodLevel + hungerRestored));
    
        int startFoodBars = Math.max(0, foodLevel / 2);
		int endFoodBars = (int) Math.ceil(modifiedFood / 2.0F);

        int iconStartOffset = 16;
		int iconSize = 9;
		for (int i = startFoodBars; i < endFoodBars; ++i)
		{
			IntPoint offset = HUDOverlayHandler.foodBarOffsets.get(i);
			if (offset == null)
				continue;

			int x = right + offset.x;
			int y = top + offset.y;

			int v = 3 * iconSize;
			int u = iconStartOffset + 4 * iconSize;
			int ub = iconStartOffset + 1 * iconSize;

			if (useRottenTextures)
			{
				u += 4 * iconSize;
				ub += 12 * iconSize;
			}

			if (i * 2 + 1 == modifiedFood)
				u += 1 * iconSize;

			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha * 0.25F);
			guiGraphics.blit(TextureHelper.MC_ICONS, x, y, ub, v, iconSize, iconSize);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

			guiGraphics.blit(TextureHelper.MC_ICONS, x, y, u, v, iconSize, iconSize);
		}
        
        HUDOverlayHandler.disableAlpha(alpha);
        ci.cancel();
    }
}
