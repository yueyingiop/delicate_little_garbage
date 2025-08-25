package com.core.DLG.mixin.itemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@Mixin(BowlFoodItem.class)
public class BowlFoodItemMixin {
    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    private void fixStacking(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        // 只在堆叠数量大于1且是玩家时处理
        if (stack.getCount() > 1 && entity instanceof Player player) {
            ItemStack emptyBowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(emptyBowl)) player.drop(emptyBowl, false);// 尝试将空碗加入背包
            // 返回剩余食物
            cir.setReturnValue(stack);
        }
    }
}
