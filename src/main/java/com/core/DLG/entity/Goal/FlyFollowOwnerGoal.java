package com.core.DLG.entity.Goal;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;

public class FlyFollowOwnerGoal extends Goal {
    private final TamableAnimal tamable;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    // private float oldWaterCost;
    // private final boolean canFly;

    public FlyFollowOwnerGoal(
        TamableAnimal tamable, 
        double speedModifier, 
        float startDistance, 
        float stopDistance,
        boolean canFly
    ) {
        this.tamable = tamable;
        this.level = tamable.level();
        this.speedModifier = speedModifier;
        this.navigation = tamable.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        // this.canFly = canFly;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.tamable.getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.tamable.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !(this.tamable.distanceToSqr(this.owner) <= (double)(this.stopDistance * this.stopDistance));
        }
   }

    public void start() {
        this.timeToRecalcPath = 0;
        this.tamable.setNoGravity(true);
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
    }

    @Override
    public void tick() {
        this.tamable.getLookControl().setLookAt(this.owner, 10.0F, (float)this.tamable.getMaxHeadXRot());
        
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            
            // 计算目标位置 - 在主人上方2-3格
            double targetX = this.owner.getX();
            double targetY = this.owner.getY() + 2.5D; // 在主人头顶上方
            double targetZ = this.owner.getZ();
            
            // 确保不会飞到地面以下
            if (targetY < this.owner.getY() + 1.0D) {
                targetY = this.owner.getY() + 1.0D;
            }
            
            // 使用飞行导航移动到目标位置
            if (!this.tamable.isPassenger()) {
                if (this.tamable.distanceToSqr(this.owner) >= 144.0D) {
                    this.tryTeleport();
                } else {
                    this.navigation.moveTo(targetX, targetY, targetZ, this.speedModifier);
                }
            }
        }
    }

    private void tryTeleport() {
        BlockPos ownerPos = this.owner.blockPosition();
        
        for(int i = 0; i < 10; ++i) {
            int x = ownerPos.getX() + this.randomIntInclusive(-3, 3);
            int y = ownerPos.getY() + this.randomIntInclusive(1, 3); // 确保在主人上方
            int z = ownerPos.getZ() + this.randomIntInclusive(-3, 3);
            
            if (this.canTeleportTo(new BlockPos(x, y, z))) {
                this.tamable.moveTo((double)x + 0.5D, y, (double)z + 0.5D, this.tamable.getYRot(), this.tamable.getXRot());
                this.navigation.stop();
                return;
            }
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        // 飞行生物可以传送到任何有空气的位置
        if (!this.level.isEmptyBlock(pos)) {
            return false;
        } else {
            BlockPos below = pos.below();
            return this.level.getBlockState(below).isSolidRender(this.level, below) || 
                   this.level.getBlockState(below).getBlock() instanceof LeavesBlock;
        }
    }

    private int randomIntInclusive(int min, int max) {
        return this.tamable.getRandom().nextInt(max - min + 1) + min;
    }
}
