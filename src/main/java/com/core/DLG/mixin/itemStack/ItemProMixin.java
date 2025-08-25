package com.core.DLG.mixin.itemStack;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.item.Item;

@Mixin(Item.Properties.class)
public abstract class ItemProMixin {
    @Shadow
    int maxStackSize;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(CallbackInfo ci) throws IOException {
        ItemConfig.init();
        this.maxStackSize = ItemConfig.getMaxStackSize();
    }

    @Inject(method = "stacksTo", at = @At("RETURN"))
    public void stacksTo(int StackSize, CallbackInfoReturnable<Item.Properties> cir) throws IOException {
        ItemConfig.init();
        this.maxStackSize = ItemConfig.getMaxStackSize();
    }
}
