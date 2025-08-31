package com.core.DLG.mixin.player;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public int attackStrengthTicker;

    @Shadow
    public abstract ItemStack getMainHandItem();

    // 取消玩家以外的实体的无敌时间
    @Inject(method = "baseTick", at = @At("TAIL"))
    private void baseTick(CallbackInfo ci) throws IOException {
        PlayerConfig.init();
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player) && PlayerConfig.getCancelInvulnerableDuration()) {
            entity.invulnerableTime = 0;
        }
    }


    // 玩家攻击间隔取消
    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) throws IOException {
        PlayerConfig.init();
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack itemStack = getMainHandItem();

        if (isSwordOrAxe(itemStack) && PlayerConfig.getCancelAttackGap() && entity instanceof Player) {
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
