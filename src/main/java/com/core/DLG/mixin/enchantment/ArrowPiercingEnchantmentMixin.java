package com.core.DLG.mixin.enchantment;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.EnchantmentConfig;

import net.minecraft.world.item.enchantment.ArrowPiercingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MultiShotEnchantment;

@Mixin(ArrowPiercingEnchantment.class)
public class ArrowPiercingEnchantmentMixin {
    @Inject(method = "checkCompatibility", at = @At("HEAD"), cancellable = true)
    private void onCheckCompatibility(Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) throws IOException {
        EnchantmentConfig.init();
        if (enchantment instanceof MultiShotEnchantment && enchantment != (Enchantment)(Object)this && EnchantmentConfig.getPierceAndMutiShooting()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
