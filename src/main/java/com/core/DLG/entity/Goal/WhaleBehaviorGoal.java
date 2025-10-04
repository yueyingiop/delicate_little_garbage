package com.core.DLG.entity.Goal;

import java.util.EnumSet;

import com.core.DLG.entity.CloudWhaleEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WhaleBehaviorGoal extends Goal {
    private final CloudWhaleEntity whale;
    private final PathNavigation navigation;
    private LivingEntity owner;

    // 游荡相关变量
    private Vec3 wanderCenter; // 游荡中心点
    private BlockPos nextWanderPos; // 下一个游荡目标点
    private int wanderCooldown = 0; // 游荡冷却
    private static final int WANDER_INTERVAL = 100; // 每5秒重新选择目标（100tick = 5秒）
    private static final double WANDER_RADIUS = 16.0; // 游荡半径

    // 状态切换相关
    private BehaviorState currentState = BehaviorState.FOLLOWING;
    private int stateSwitchCooldown = 0;
    private static final int SWITCH_COOLDOWN = 10; // 防止快速切换

    public WhaleBehaviorGoal(CloudWhaleEntity whale) {
        this.whale = whale;
        this.navigation = whale.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 只有被驯服的鲸鱼才使用这个目标
        return this.whale.isTame() && this.whale.getOwner() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.whale.isTame() && this.whale.getOwner() != null && !this.whale.isInSittingPose();
    }

    @Override
    public void start() {
        this.owner = this.whale.getOwner();
        this.stateSwitchCooldown = 0;

        // 初始化游荡中心为当前位置
        if (this.wanderCenter == null) {
            this.wanderCenter = this.whale.position();
        }

        // 初始化状态
        updateBehavior();
    }

    @Override
    public void stop() {
        this.navigation.stop();
        this.owner = null;
    }

    @Override
    public void tick() {
        if (stateSwitchCooldown > 0) {
            stateSwitchCooldown--;
        }

        if (wanderCooldown > 0) {
            wanderCooldown--;
        }
        
        if (owner == null) {
            owner = whale.getOwner();
            if (owner == null) return;
        }
        
        // 根据当前状态执行不同行为
        switch (currentState) {
            case FOLLOWING -> executeFollowing();
            case WANDERING -> executeWandering();
            case SITTING -> executeSitting();
        }
        
        // 注视主人
        if (currentState != BehaviorState.SITTING) {
            this.whale.getLookControl().setLookAt(owner, 10.0F, (float)this.whale.getMaxHeadXRot());
        }
    }

    // 跟随行为
    private void executeFollowing() {
        if (owner == null) return;
        
        // 10%概率以玩家为中心进行小范围游荡
        if (whale.getRandom().nextDouble() < 0.1) {
            // 使用玩家位置作为游荡中心，半径为8进行游荡
            selectNewWanderTarget(owner.position(), 8.0);
            
            // 如果当前有游荡目标，尝试移动过去
            if (nextWanderPos != null) {
                this.navigation.moveTo(
                    nextWanderPos.getX(), 
                    nextWanderPos.getY(), 
                    nextWanderPos.getZ(), 
                    0.4D
                );
            }
        } else {
            // 正常的跟随行为
            // 计算目标位置 - 在主人上方3-5格
            double targetX = owner.getX();
            double targetY = owner.getY() + 2.5D; // 在主人头顶上方2.5格
            double targetZ = owner.getZ();
            
            // 确保不会飞到地面以下
            if (targetY < owner.getY() + 2.0D) {
                targetY = owner.getY() + 2.0D;
            }
            
            // 如果距离较远，移动到主人附近
            double distanceSqr = whale.distanceToSqr(owner);
            if (distanceSqr > 100.0D) { // 10格距离
                whale.setPos(targetX, targetY, targetZ); // 瞬移到指定位置
            } else if (distanceSqr > 25.0D) { // 5格距离
                this.navigation.moveTo(targetX, targetY, targetZ, 0.8D);
            } else if (distanceSqr > 9.0D) { // 3格距离，保持距离
                this.navigation.moveTo(targetX, targetY, targetZ, 0.4D);
            }
        }
    }

    // 游荡行为
    private void executeWandering() {
        // 如果导航已完成或冷却结束，选择新的游荡目标
        if (this.navigation.isDone() || wanderCooldown <= 0) {
            // 50%概率原地等待，50%概率选择新目标
            if (whale.getRandom().nextDouble() < 0.5) {
                // 原地等待：停止移动并设置等待时间
                this.navigation.stop();
                wanderCooldown = WANDER_INTERVAL / 2; // 等待时间减半（2.5秒）
            } else {
                // 选择新目标继续游荡
                selectNewWanderTarget();
                wanderCooldown = WANDER_INTERVAL;
            }
        }
        
        // 如果当前有游荡目标，尝试移动过去
        if (nextWanderPos != null) {
            this.navigation.moveTo(
                nextWanderPos.getX(), 
                nextWanderPos.getY(), 
                nextWanderPos.getZ(), 
                0.3D
            );
        }
    }

    // 默认 - 选择新的游荡目标
    private void selectNewWanderTarget() {
        if (wanderCenter == null) {
            wanderCenter = whale.position();
        }
        this.selectNewWanderTarget(wanderCenter, WANDER_RADIUS);
    }

    // 选择新的游荡目标
    private void selectNewWanderTarget(Vec3 center, double radius) {
        // 在游荡中心半径radius格内随机选择目标点
        double angle = whale.getRandom().nextDouble() * 2 * Math.PI;
        double distance = whale.getRandom().nextDouble() * radius;
        
        double targetX = center.x + Math.cos(angle) * distance;
        double targetZ = center.z + Math.sin(angle) * distance;
        
        // 保持当前高度附近，避免大幅度的垂直移动
        double minY = center.y; // 最小高度为游荡中心
        double maxY = center.y + 8.0; // 最大高度为游荡中心上方8格
        double targetY = minY + whale.getRandom().nextDouble() * (maxY - minY);
        
        nextWanderPos = new BlockPos((int)targetX, (int)targetY, (int)targetZ);
        
        // 更新游荡中心为当前位置（缓慢移动游荡区域）
        // if (whale.getRandom().nextInt(4) == 0) { // 25%概率更新游荡中心
        //     center = whale.position();
        // }
    }

    // 坐下行为
    private void executeSitting() {
        // 完全停止所有移动
        this.navigation.stop();
        this.whale.setDeltaMovement(0, 0, 0);
        
        if (whale.level().isClientSide) {
            // 客户端浮动效果
            float floatAmount = (float)Math.sin(whale.tickCount * 0.1F) * 0.02F;
            whale.setDeltaMovement(whale.getDeltaMovement().x, floatAmount, whale.getDeltaMovement().z);
        }
    }

    // 切换状态的方法
    public void switchBehavior() {
        if (stateSwitchCooldown > 0) return;
        
        this.currentState = this.currentState.next();
        this.stateSwitchCooldown = SWITCH_COOLDOWN;

        // 切换到游荡模式时，设置游荡中心为当前位置
        if (this.currentState == BehaviorState.WANDERING) {
            this.wanderCenter = this.whale.position();
            this.wanderCooldown = 0; // 立即选择新目标
        }
        
        updateBehavior();
        
        // 发送状态切换消息给主人
        if (owner instanceof Player player) {
            Component component = Component.translatable(
                "tips.dlg.cloud_whale.update_behavior",
                Component.translatable("tips.dlg.cloud_whale." + currentState.getDisplayName())
            );
            player.displayClientMessage(component, true);
        }
    }

    // 根据状态更新行为
    private void updateBehavior() {
        switch (currentState) {
            case FOLLOWING -> {
                this.navigation.stop(); // 先停止之前的移动
                // 跟随模式会重新开始移动
            }
            case WANDERING -> {
                this.navigation.stop();
                this.wanderCooldown = 0; // 重置游荡冷却，立即选择新目标

            }
            case SITTING -> {
                this.navigation.stop();
                this.whale.setDeltaMovement(0, 0, 0);
            }
        }
    }

    // 获取当前状态
    public BehaviorState getCurrentState() {
        return currentState;
    }
    
    // 设置状态（用于加载保存数据）
    public void setState(BehaviorState state) {
        this.currentState = state;
        if (state == BehaviorState.WANDERING) {
            this.wanderCenter = this.whale.position();
        }
        updateBehavior();
    }

    // 获取游荡中心
    public Vec3 getWanderCenter() {
        return wanderCenter;
    }

    // 设置游荡中心（用于保存数据）
    public void setWanderCenter(Vec3 center) {
        this.wanderCenter = center;
    }
    
    // 检查是否在跟随模式
    public boolean isFollowing() {
        return currentState == BehaviorState.FOLLOWING;
    }
    
    // 检查是否在游荡模式
    public boolean isWandering() {
        return currentState == BehaviorState.WANDERING;
    }
    
    // 检查是否在坐下模式
    public boolean isSitting() {
        return currentState == BehaviorState.SITTING;
    }

    // 行为状态
    public enum BehaviorState {
        FOLLOWING(0, "follow"),
        WANDERING(1, "wander"), 
        SITTING(2, "wait");
        
        private final int id;
        private final String displayName;
        
        BehaviorState(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }
        
        public int getId() { return id; }
        public String getDisplayName() { return displayName; }
        
        public static BehaviorState getById(int id) {
            for (BehaviorState state : values()) {
                if (state.id == id) return state;
            }
            return FOLLOWING;
        }
        
        public BehaviorState next() {
            return getById((this.id + 1) % values().length);
        }
    }

}
