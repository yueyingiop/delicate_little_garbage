package com.core.DLG.mixin.itemStack;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public abstract class ItemMixin {
    // 添加物品提示
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void StackCountTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (stack.getCount() >= 1000) {
            tooltip.add(Component.translatable("tooltip.dlg.stackcount", stack.getCount())); // 添加物品提示
        }
    }
}
