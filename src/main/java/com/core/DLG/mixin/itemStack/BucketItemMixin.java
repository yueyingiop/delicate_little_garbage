package com.core.DLG.mixin.itemStack;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void fixStacking(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        // 只在堆叠数量大于1时处理
        if (stack.getCount() > 1) {
            ItemStack emptyBucket = new ItemStack(net.minecraft.world.item.Items.BUCKET);// 创建一个空桶
            stack.shrink(1); // 原桶-1
            if (!player.getInventory().add(emptyBucket)) player.drop(emptyBucket, false);// 如果玩家背包有空间，则添加空桶,否则掉落
            cir.setReturnValue(InteractionResultHolder.sidedSuccess(stack, level.isClientSide()));
        }
    }
}