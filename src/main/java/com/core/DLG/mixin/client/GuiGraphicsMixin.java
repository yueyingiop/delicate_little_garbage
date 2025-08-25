package com.core.DLG.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(
        method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At("HEAD")
    )
    private void renderBefore(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        GuiGraphics self = (GuiGraphics) (Object) this;
        int len = formatStackCount(stack.getCount()).length();
        if(len < 6) return;
        self.pose().pushPose();
        switch (len) {
            case 6://1.0b
                self.pose().translate(x + 9, y + 9, 0);
                self.pose().scale(0.8f, 0.8f, 1);
                break;
            case 7://10.0b
            self.pose().translate(x + 10, y + 10, 0);
                self.pose().scale(0.7f, 0.7f, 1);
                break;
            case 8://100.0b
            self.pose().translate(x + 12, y + 11, 0);
                self.pose().scale(0.6f, 0.6f, 1);
                break;
        }
        self.pose().translate(-x - 8, -y - 8, 0);
    }

    @Inject(
        method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At("TAIL")
    )
    private void renderAfter(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        GuiGraphics self = (GuiGraphics) (Object) this;
        int len = formatStackCount(stack.getCount()).length();
        if(len < 6) return;
        self.pose().popPose();
    }

    @ModifyVariable(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    public String itemStackTextStringFormat(String original, Font font, ItemStack stack, int x, int y, String countOverride) {
        int stackCount = stack.getCount();

        return formatStackCount(original, stackCount);
    }

    @Unique
    private String formatStackCount(String original, int stackCount) { 
        if (stackCount >= 1000000000) {
            double value = stackCount / 1000000000.0;
            return formatNumber(value) + "§6B";
        } else if (stackCount >= 1000000) {
            double value = stackCount / 1000000.0;
            return formatNumber(value) + "§3M";
        } else if (stackCount >= 1000) {
            double value = stackCount / 1000.0;
            return formatNumber(value) + "§9K";
        } else {
            return original;
        }
    }

    @Unique
    private String formatStackCount(int stackCount) {
        if (stackCount >= 1000000000) {
            double value = stackCount / 1000000000.0;
            return formatNumber(value) + "§6B";
        } else if (stackCount >= 1000000) {
            double value = stackCount / 1000000.0;
            return formatNumber(value) + "§3M";
        } else if (stackCount >= 1000) {
            double value = stackCount / 1000.0;
            return formatNumber(value) + "§9K";
        } else {
            return formatNumber(stackCount);
        }
    }

    @Unique
    private String formatNumber(double value) {
        if (value == (int) value) {
            return String.valueOf((int) value);
        }
        return String.format("%.1f", value);
    }
}
