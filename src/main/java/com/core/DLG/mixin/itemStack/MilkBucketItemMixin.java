package com.core.DLG.mixin.itemStack;

import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {
    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    private void fixStacking(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        // 只在堆叠数量大于1且是玩家时处理
        if (stack.getCount() > 1 && entity instanceof Player player) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            if (!player.getInventory().add(emptyBucket)) player.drop(emptyBucket, false);// 尝试将空桶加入背包
            // 返回剩余牛奶桶
            cir.setReturnValue(stack);
        }
    }
}