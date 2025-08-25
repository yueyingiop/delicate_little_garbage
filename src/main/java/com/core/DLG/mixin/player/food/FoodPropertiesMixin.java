package com.core.DLG.mixin.player.food;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.world.food.FoodProperties;

@Mixin(FoodProperties.class)
public abstract class FoodPropertiesMixin {
    @Shadow
    private boolean canAlwaysEat;

    @Inject(method = "canAlwaysEat",at = @At("RETURN"), cancellable = true)
    private void canAlwaysEat(CallbackInfoReturnable<Boolean> cir) throws IOException { 
        PlayerConfig.init();
        if (PlayerConfig.getAlwaysEat()) {
            cir.setReturnValue(true);
        }
    }
}
