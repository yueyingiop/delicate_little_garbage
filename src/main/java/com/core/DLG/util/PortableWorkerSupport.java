package com.core.DLG.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface PortableWorkerSupport {
    /**
     * 当玩家使用便携工作物品尝试打开此方块的GUI时调用。
     * @param player 玩家
     * @param level 世界
     * @param pos 一个参考位置（例如玩家的位置）
     */
    void openPortableGui(ServerPlayer player, Level level, BlockPos pos);
}
