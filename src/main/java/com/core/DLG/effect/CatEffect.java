package com.core.DLG.effect;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class CatEffect extends MobEffect {

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

        for(Cat cat : level.getEntitiesOfClass(Cat.class, searchArea)) {
            if (entity instanceof Player player) {
                // 移除猫的逃跑目标（如果目标是这个玩家）
                if (cat.getLastHurtByMob() == player) {
                    cat.setLastHurtByMob(null);
                }
                
                // 让猫信任玩家（停止逃跑并靠近）
                // cat.setTrusting(true);
            }

            cat.getNavigation().moveTo(entity.getX(), entity.getY(), entity.getZ(), 1.0D);
        }

        for(Ocelot ocelot : level.getEntitiesOfClass(Ocelot.class, searchArea)) {
            if (entity instanceof Player player) {
                // 移除豹猫的逃跑目标（如果目标是这个玩家）
                if (ocelot.getLastHurtByMob() == player) {
                    ocelot.setLastHurtByMob(null);
                }

                // 让豹猫信任玩家（停止逃跑并靠近）
                // ocelot.setTrusting(true);
            }

            ocelot.getNavigation().moveTo(entity.getX(), entity.getY(), entity.getZ(), 1.0D);
        }
    }
}
