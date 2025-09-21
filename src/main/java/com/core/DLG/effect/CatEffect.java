package com.core.DLG.effect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class CatEffect extends MobEffect {
    private static final Map<UUID, Set<UUID>> playerAffectedCats = new HashMap<>();

    protected CatEffect() {
        super(
            MobEffectCategory.NEUTRAL,
            0xf3e4fe
        );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;

        ServerLevel level = (ServerLevel) entity.level();

        AABB searchArea = new AABB(entity.blockPosition()).inflate(10.0D);

        MobEffectInstance effectInstance = entity.getEffect(this);
        if (effectInstance == null) return;

        int remainingDuration = effectInstance.getDuration();
        boolean isEffectEnding = remainingDuration < 20;
        if (entity instanceof Player player) {
            // 获取玩家已处理的猫
            Set<UUID> affectedCats = playerAffectedCats.getOrDefault(player.getUUID(), new HashSet<>());

            // 获取范围内所有猫
            Set<UUID> currentCatsInRange = new HashSet<>();
            for(Cat cat : level.getEntitiesOfClass(Cat.class, searchArea)) {
                currentCatsInRange.add(cat.getUUID());

                // 移除猫的逃跑目标（如果目标是这个玩家）
                if (cat.getLastHurtByMob() == player) {
                    cat.setLastHurtByMob(null);
                }
                
                // 让猫信任玩家（停止逃跑并靠近）
                if (isEffectEnding) {
                    cat.setTame(false);
                    cat.setOwnerUUID(null);
                    cat.setOrderedToSit(false);
                    cat.setInSittingPose(false);
                } else {
                    if (!cat.isTame()) {
                        cat.setTame(true);
                        cat.setOwnerUUID(player.getUUID());
                        cat.setOrderedToSit(false);
                        cat.setInSittingPose(false);
                        affectedCats.add(cat.getUUID());
                    }
                }

                // 如果猫不在坐下，则让猫移动
                if (!cat.isOrderedToSit() || !cat.isInSittingPose()) {
                    cat.getNavigation().moveTo(entity.getX(), entity.getY(), entity.getZ(), 1.0D);
                }
            }
        
            // 移除不再范围内的猫
            Set<UUID> catsToRemove = new HashSet<>(affectedCats);
            catsToRemove.removeAll(currentCatsInRange);

            for(UUID catId : catsToRemove) {
                Entity catEntity = level.getEntity(catId);
                if (catEntity instanceof Cat cat) {
                    cat.setTame(false);
                    cat.setOwnerUUID(null);
                    cat.setOrderedToSit(false);
                    cat.setInSittingPose(false);
                    affectedCats.remove(catId); // 从跟踪集合中移除
                }
            }

            // 更新全局跟踪Map
            playerAffectedCats.put(player.getUUID(), affectedCats);
        }

        for(Ocelot ocelot : level.getEntitiesOfClass(Ocelot.class, searchArea)) {
            if (entity instanceof Player player) {
                // 移除豹猫的逃跑目标（如果目标是这个玩家）
                if (ocelot.getLastHurtByMob() == player) {
                    ocelot.setLastHurtByMob(null);
                }

                // 药水时间快结束时删除豹猫的“信任”
                // 让豹猫信任玩家（停止逃跑并靠近）
                if (isEffectEnding) {
                    ocelot.setTrusting(false);
                } else {
                    ocelot.setTrusting(true);
                }
            }

            ocelot.getNavigation().moveTo(entity.getX(), entity.getY(), entity.getZ(), 1.0D);
        }
    }
}
