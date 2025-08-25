package com.core.DLG.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.core.DLG.configs.ItemConfig;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {
    @Inject(method = "addCooldown", at = @At("HEAD"), cancellable = true)
    private void cancelCooldown(Item item, int ticks, CallbackInfo ci) throws IOException{
        ItemConfig.init();
        if (ItemConfig.getItemCooldowns()) {
            ci.cancel();
        }
    }
}