package com.core.DLG.mixin.itemStack;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.core.DLG.block.entity.CraftingBlockEntity;
import com.core.DLG.enums.TypeEnum;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public abstract class ItemMixin {
    // 添加物品提示(物品堆叠数提示)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void StackCountTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (stack.getCount() >= 1000) {
            tooltip.add(Component.translatable("tooltip.dlg.stackcount", stack.getCount())); // 添加物品提示
        }
    }

    // 添加物品提示(绑定后经验和等级提示)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void ExpTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) { 
        if (CraftingBlockEntity.hasDebris(stack)) {
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.dlg.upgrade").withStyle(ChatFormatting.GOLD));
            }
            // 按下键Ctrl查看经验
            if (Screen.hasShiftDown()){
                CompoundTag tag = stack.getTag();
                if (tag == null) return;
                CompoundTag debrisTag = tag.getCompound("boundDebris");
                // 绑定类型
                tooltip.add(
                    Component.translatable(
                        "tooltip.dlg.extend",
                        Component.translatable(
                            "tooltip.dlg.debris.type." + debrisTag.getString("type")
                        ).withStyle(
                            Style.EMPTY.withColor(
                                TypeEnum.getType(debrisTag.getString("type")).getColor()
                            )
                        )
                    ).withStyle(ChatFormatting.GREEN)
                );
                // 显示经验
                tooltip.add(
                    Component.translatable(
                        "tooltip.dlg.exp", 
                        debrisTag.getInt("exp"), 
                        debrisTag.getInt("maxExp")
                    ).withStyle(ChatFormatting.GREEN)
                );
                // 显示等级
                tooltip.add(
                    Component.translatable(
                        "tooltip.dlg.level", 
                        debrisTag.getInt("level"),
                        debrisTag.getInt("maxLevel")
                    ).withStyle(ChatFormatting.GREEN)
                );
            }
        }
    }
}
