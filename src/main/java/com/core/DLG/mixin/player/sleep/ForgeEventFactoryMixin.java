package com.core.DLG.mixin.player.sleep;

import java.io.IOException;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;


@Mixin(ForgeEventFactory.class)
public abstract class ForgeEventFactoryMixin {
    /**
     * @author yueyiniop
     * @reason 修改玩家休眠机制 - 取消白天唤醒玩家
     */
    @Overwrite(remap = false)
    public static boolean fireSleepingLocationCheck(LivingEntity player, BlockPos sleepingLocation) throws IOException {
        PlayerConfig.init();
        SleepingLocationCheckEvent evt = new SleepingLocationCheckEvent(player, sleepingLocation);
        MinecraftForge.EVENT_BUS.post(evt);
        if (PlayerConfig.getAlwaysSleep()) {
            return true;
        } else {
            Result canContinueSleep = evt.getResult();
            if (canContinueSleep == Result.DEFAULT)
            {
                return player.getSleepingPos().map(pos-> {
                    BlockState state = player.level().getBlockState(pos);
                    return state.getBlock().isBed(state, player.level(), pos, player);
                }).orElse(false);
            }
            else
                return canContinueSleep == Result.ALLOW;
        }
        
    }

    /**
     * @author yueyiniop
     * @reason 修改玩家休眠机制 - 取消白天唤醒玩家
     */
    @Overwrite(remap = false)
    public static boolean fireSleepingTimeCheck(Player player, Optional<BlockPos> sleepingLocation) throws IOException {
        PlayerConfig.init();
        SleepingTimeCheckEvent evt = new SleepingTimeCheckEvent(player, sleepingLocation);
        MinecraftForge.EVENT_BUS.post(evt);
        if (PlayerConfig.getAlwaysSleep()) {
            return true;
        } else {
            Result canContinueSleep = evt.getResult();
        if (canContinueSleep == Result.DEFAULT)
            return !player.level().isDay();
        else
            return canContinueSleep == Result.ALLOW;
        }
    }
}
