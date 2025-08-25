package com.core.DLG.mixin.itemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;

@Mixin(SuspiciousStewItem.class)
public class SuspiciousStewItemMixin {
    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    private void fixStacking(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        // 只在堆叠数量大于1时处理
        if (stack.getCount() > 1  && entity instanceof Player player) {
            ItemStack emptyBucket = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(emptyBucket)) player.drop(emptyBucket, false);// 如果玩家背包有空间，则添加空桶,否则掉落
            cir.setReturnValue(stack);
        }
    }
}
