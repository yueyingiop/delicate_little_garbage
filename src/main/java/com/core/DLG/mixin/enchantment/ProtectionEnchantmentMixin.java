package com.core.DLG.mixin.enchantment;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.EnchantmentConfig;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
    @Inject(method = "checkCompatibility", at = @At("HEAD"), cancellable = true)
    private void onCheckCompatibility(Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) throws IOException {
        EnchantmentConfig.init();
        if (enchantment instanceof ProtectionEnchantment && enchantment != (Enchantment)(Object)this && EnchantmentConfig.getProtection()) {
            cir.setReturnValue(true);
        }
    }
}
