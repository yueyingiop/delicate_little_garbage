package com.core.DLG.inventory;

import javax.annotation.Nonnull;

import com.core.DLG.DLG;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CraftingBlockScreen extends AbstractContainerScreen<CraftingBlockMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DLG.MODID, "textures/gui/debris_smithing_table.png");

    public CraftingBlockScreen(CraftingBlockMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    // 加载背景
    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    // 渲染界面
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    // 初始化UI元素
    @Override
    protected void init() {
        super.init();
        // 初始化UI元素
    }

}
