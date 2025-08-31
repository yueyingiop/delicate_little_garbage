package com.core.DLG.mixin.player.sleep;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.world.level.dimension.DimensionType;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = "bedWorks", at = @At("RETURN"), cancellable = true)
    public void onBedWorks(CallbackInfoReturnable<Boolean> cir) throws IOException {
        PlayerConfig.init();
        if (PlayerConfig.getSleepEverywhere()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "natural", at = @At("RETURN"), cancellable = true)
    public void onNatural(CallbackInfoReturnable<Boolean> cir) throws IOException {
        PlayerConfig.init();
        if (PlayerConfig.getSleepEverywhere()) {
            cir.setReturnValue(true);
        }
    }
}
