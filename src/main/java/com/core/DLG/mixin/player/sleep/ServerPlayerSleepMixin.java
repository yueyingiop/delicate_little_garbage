package com.core.DLG.mixin.player.sleep;

import java.io.IOException;
// import java.util.List;
import java.util.Optional;

// import javax.annotation.Nonnull;
// import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Overwrite;
// import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.core.DLG.configs.PlayerConfig;
// import com.mojang.authlib.GameProfile;
// import com.mojang.datafixers.util.Either;

// import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
// import net.minecraft.core.Direction;
// import net.minecraft.network.chat.Component;
// import net.minecraft.resources.ResourceKey;
// import net.minecraft.server.MinecraftServer;
// import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
// import net.minecraft.stats.Stats;
// import net.minecraft.util.Unit;
// import net.minecraft.world.entity.monster.Monster;
// import net.minecraft.world.entity.player.Player;
// import net.minecraft.world.level.Level;
// import net.minecraft.world.level.block.HorizontalDirectionalBlock;
// import net.minecraft.world.phys.AABB;
// import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

@Mixin(ServerPlayer.class)
// public abstract class ServerPlayerSleepMixin extends Player {
//
//     public ServerPlayerSleepMixin(MinecraftServer mcServer, ServerLevel serverLevel, GameProfile gameProfile) {
//         super(serverLevel, serverLevel.getSharedSpawnPos(), serverLevel.getSharedSpawnAngle(), gameProfile);
//     }
//
//     /**
//      * @author yueyiniop
//      * @reason 修改玩家休眠机制
//      */
//     @Overwrite
//     public Either<Player.BedSleepingProblem, Unit> startSleepInBed(@Nonnull BlockPos pos) {
//         java.util.Optional<BlockPos> optAt = java.util.Optional.of(pos);
//         Player.BedSleepingProblem ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(this, optAt);
//         if (ret != null)
//             return Either.left(ret);
//         Direction direction = this.level().getBlockState(pos).getValue(HorizontalDirectionalBlock.FACING);
//         if (!this.isSleeping() && this.isAlive()) {
//             if (!this.level().dimensionType().natural()) {
//                 return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
//             } else if (!this.bedInRange(pos, direction)) {
//                 return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
//             } else if (this.bedBlocked(pos, direction)) {
//                 return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
//             } else {
//                 this.setRespawnPosition(this.level().dimension(), pos, this.getYRot(), false, true);
//                 if (!net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(this, optAt) && !PlayerConfig.getAlwaysSleep()) {
//                     return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
//                 } else {
//                     if (!this.isCreative()) {
//                         // double d0 = 8.0D;
//                         // double d1 = 5.0D;
//                         Vec3 vec3 = Vec3.atBottomCenterOf(pos);
//                         List<Monster> list = this.level()
//                                 .getEntitiesOfClass(Monster.class, new AABB(vec3.x() - 8.0D, vec3.y() - 5.0D,
//                                         vec3.z() - 8.0D, vec3.x() + 8.0D, vec3.y() + 5.0D, vec3.z() + 8.0D),
//                                         (p_9062_) -> {
//                                             return p_9062_.isPreventingPlayerRest(this);
//                                         });
//                         if (!list.isEmpty()) {
//                             return Either.left(Player.BedSleepingProblem.NOT_SAFE);
//                         }
//                     }
//
//                     Either<Player.BedSleepingProblem, Unit> either = super.startSleepInBed(pos).ifRight((p_9029_) -> {
//                         this.awardStat(Stats.SLEEP_IN_BED);
//                         CriteriaTriggers.SLEPT_IN_BED.trigger((ServerPlayer) (Object) this);
//                     });
//                     if (!this.serverLevel().canSleepThroughNights()) {
//                         this.displayClientMessage(Component.translatable("sleep.not_possible"), true);
//                     }
//
//                     ((ServerLevel) this.level()).updateSleepingPlayerList();
//                     return either;
//                 }
//             }
//         } else {
//             return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
//         }
//     }
//
//     @Shadow
//     public abstract boolean bedInRange(BlockPos pos, Direction direction);
//
//     @Shadow
//     public abstract boolean bedBlocked(BlockPos pos, Direction direction);
//
//     @Shadow
//     public abstract void setRespawnPosition(ResourceKey<Level> levelKey, @Nullable BlockPos pos, float respawnAngle, boolean forced, boolean sendMessage);
//
//     @Shadow
//     public abstract ServerLevel serverLevel();
// }


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