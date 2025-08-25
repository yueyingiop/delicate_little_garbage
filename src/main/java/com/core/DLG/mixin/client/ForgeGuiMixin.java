package com.core.DLG.mixin.client;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.core.DLG.configs.PlayerConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.gui.overlay.ForgeGui;
// import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(ForgeGui.class)
public abstract class ForgeGuiMixin extends Gui {
    public int leftHeight = 39;
    public int rightHeight = 39;
    
    public ForgeGuiMixin(Minecraft mc)
    {
        super(mc, mc.getItemRenderer());
    }
    
    /**
     * @author yueyingiop
     * @reason 修改饥饿值显示
    */ 
    @Overwrite(remap = false)
    public void renderFood(int width, int height, GuiGraphics guiGraphics) throws IOException { 
        PlayerConfig.init();
        this.minecraft.getProfiler().push("food");
        
        Player player = (Player) this.minecraft.getCameraEntity();
        if (player == null || this.minecraft.player == null) {
            this.minecraft.getProfiler().pop();
            return;
        }

        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - rightHeight;
        rightHeight += 10;
        boolean unused = false;

        FoodData stats = player.getFoodData();
        int level = stats.getFoodLevel();
        int maxLevel = PlayerConfig.getMaxHungry();
        if (maxLevel >= 1000) maxLevel = 1000;//设置最大渲染数，避免卡顿
        for (int num = (int) Math.ceil(maxLevel / 2.0); num > 0; num -= 10) {//10个图标一组
            for (int i = 0; i < (num - 10 > 0 ? 10 : num); ++i){
                int idx = (i + (((int) Math.ceil(maxLevel / 2.0)) - num)) * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte background = 0;

                if (player.hasEffect(MobEffects.HUNGER))
                {
                    icon += 36;
                    background = 13;
                }

                if (unused) background = 1;

                if (player.getFoodData().getSaturationLevel() <= 0.0F && tickCount % (level * 3 + 1) == 0)
                {
                    y = top + (random.nextInt(3) - 1);
                }
                guiGraphics.blit(GUI_ICONS_LOCATION, x, y, 16 + background * 9, 27, 9, 9);

                if (idx < level)
                    guiGraphics.blit(GUI_ICONS_LOCATION, x, y, icon + 36, 27, 9, 9);
                else if (idx == level)
                    guiGraphics.blit(GUI_ICONS_LOCATION, x, y, icon + 45, 27, 9, 9);
            }
            top -= 9;//往上一排排渲染图标
        }
        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
}
