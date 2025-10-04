package com.core.DLG.mixin.player.sleep;

import java.io.IOException;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerSleepMixin {

    // 修改玩家休眠时间限制
    @Redirect(
        method = "startSleepInBed(Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/event/ForgeEventFactory;fireSleepingTimeCheck(Lnet/minecraft/server/level/ServerPlayer;Ljava/util/Optional;)Z"
        )
    )
    private boolean modifySleepTimeCheck(ServerPlayer player, Optional<BlockPos> pos) throws IOException {
        PlayerConfig.init();
        boolean originalResult = ForgeEventFactory.fireSleepingTimeCheck(player, pos);
        return !originalResult && PlayerConfig.getAlwaysSleep();
    }
}