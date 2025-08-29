package com.core.DLG.mixin.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

@Mixin(LivingEntity.class)
public abstract class PlayerMixin {
    @Shadow
    public int attackStrengthTicker;

    @Shadow
    public abstract ItemStack getMainHandItem();


    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack itemStack = getMainHandItem();

        if (isSwordOrAxe(itemStack) && entity instanceof Player) {
            int requiredTicks = Integer.MAX_VALUE/2;
            if (this.attackStrengthTicker < requiredTicks) {
                this.attackStrengthTicker = requiredTicks;
            }
        }
    }

    private boolean isSwordOrAxe(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        return itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem;
    }
}
