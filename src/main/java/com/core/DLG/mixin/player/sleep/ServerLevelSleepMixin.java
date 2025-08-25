package com.core.DLG.mixin.player.sleep;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(ServerLevel.class)
public abstract class ServerLevelSleepMixin {
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"
        )
    )
    private void redirectSetDayTime(ServerLevel serverLevel, long originalTime) throws IOException {
        PlayerConfig.init();
        GameRules gameRules = serverLevel.getGameRules();
        if (gameRules.getBoolean(GameRules.RULE_DAYLIGHT)) {
            if (PlayerConfig.getAlwaysSleep()) {
                long newTime = (serverLevel.getDayTime() + PlayerConfig.getSleepDurationTime()) % 24000;
                serverLevel.setDayTime(ForgeEventFactory.onSleepFinished(serverLevel, newTime, serverLevel.getDayTime()));
            } else {
                long j = serverLevel.getDayTime() + 24000L;
                serverLevel.setDayTime(ForgeEventFactory.onSleepFinished(serverLevel, j - j % 24000L, serverLevel.getDayTime()));
            }
        }
    }
}
