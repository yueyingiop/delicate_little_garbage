package com.core.DLG.util.damageHUD;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.core.DLG.DLG;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = DLG.MODID)
public class DamageTracker {
    private static final Map<UUID, PlayerDamageData> playerDamageMap = new HashMap<>();
    private static final int RESET_TIME_TICKS = 100;

    static {
        DLG.LOGGER.info("DamageTracker initialized");
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            float damage = event.getAmount();
            PlayerDamageData damageData = playerDamageMap.computeIfAbsent(
                player.getUUID(), 
                k -> new PlayerDamageData()
            );

            long currentTime = player.level().getGameTime();
            if (currentTime - damageData.lastDamageTime > RESET_TIME_TICKS) {
                damageData.totalDamage = 0; // 重置伤害
            }

            // 累加伤害并更新时间
            damageData.totalDamage += damage;
            damageData.lastDamageTime = currentTime;
            
            // 发送网络包给所有客户端
            DLG.NETWORK.send(
                PacketDistributor.ALL.noArg(), 
                new DamageHUDPacket(player.getUUID(), damageData.totalDamage, damageData.lastDamageTime)
            );
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // 获取当前服务器游戏时间
            long currentServerTime = 0;
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                currentServerTime = ServerLifecycleHooks.getCurrentServer().overworld().getGameTime();
            }
            
            final long finalCurrentTime = currentServerTime; // 创建final变量供lambda使用
            
            // 检查所有玩家的伤害数据是否超时
            playerDamageMap.entrySet().removeIf(entry -> {
                PlayerDamageData damageData = entry.getValue();
                // 使用游戏时间进行比较
                if (finalCurrentTime - damageData.lastDamageTime > RESET_TIME_TICKS * 2) {
                    return true;
                }
                return false;
            });
        }
    }

    public static float getTotalDamage(UUID playerId) {
        PlayerDamageData damageData = playerDamageMap.get(playerId);
        return damageData != null ? damageData.totalDamage : 0;
    }
    

    private static class PlayerDamageData {
        public float totalDamage = 0;
        public long lastDamageTime = 0;
    }
}
