package com.core.DLG.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class CloudWhaleEntity extends TamableAnimal {
    public final AnimationState moveAnimationState = new AnimationState();
    public final AnimationState jumpAnimationState = new AnimationState();
    private static final int MIN_FLYING_HEIGHT = 60;
    private static final int MAX_FLYING_HEIGHT = 100;

    private int jumpCooldown = 0;
    private static final int JUMP_COOLDOWN_MIN = 200; // 最小冷却时间 (10秒)
    private static final int JUMP_COOLDOWN_MAX = 600; // 最大冷却时间 (30秒)
    private static final float JUMP_CHANCE = 0.005f; // 每次tick触发跳跃的概率 (0.5%)

    private boolean wasBaby = false; // 是否是幼小的云鲸
    private Player attractedPlayer = null; // 被吸引的玩家
    private WaterAvoidingRandomFlyingGoal randomFlyingGoal; // 随机移动
    
    protected CloudWhaleEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true); // 禁用重力
        this.refreshDimensions(); // 更新实体的尺寸
        this.wasBaby = this.isBaby();
    }

    //#region 实体基础设定(初始化)
    // 设置运动阻力
    @Override
    public void travel(@Nonnull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isNoGravity()) {
            // 使用飞行移动控制
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
            } else {
                this.moveRelative(0.6F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
            }
        } else {
            super.travel(travelVector);
        }
    }

    // 免疫摔落伤害
    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @Nonnull DamageSource source) {
        return false;
    }

    // 实体属性
    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // 最大生命值
                .add(Attributes.FLYING_SPEED, 0.15D) // 飞行速度
                .add(Attributes.MOVEMENT_SPEED, 0.15D) // 移动速度
                .add(Attributes.FOLLOW_RANGE, 16.0D) // 跟随距离
                .add(Attributes.ATTACK_DAMAGE, 1.0D) // 攻击伤害
                .add(Attributes.ARMOR, 6.0D) // 防御
                .add(Attributes.ARMOR_TOUGHNESS, 2.0D); // 防御韧性
    }

    // 创建飞行导航
    @Override
    protected PathNavigation createNavigation(@Nonnull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@Nonnull BlockPos pos) {
                return true;
            }

            @Override
            public boolean canCutCorner(@Nonnull BlockPathTypes pathType) {
                return pathType != BlockPathTypes.DAMAGE_FIRE && pathType != BlockPathTypes.DANGER_OTHER;
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    // 是否为飞行状态
    @Override
    public boolean isFallFlying() {
        return true;
    }

    // 碰撞箱设置
    @Override
    public EntityDimensions getDimensions(@Nonnull Pose pose) {
        if (this.isBaby()) {
            return EntityDimensions.scalable(0.5F, 0.375F);
        }
        return EntityDimensions.scalable(1.0F, 0.75F);
    }

    // 设置视线高度
    @Override
    public float getEyeHeight(@Nonnull Pose pose) {
        if (this.isBaby()) {
            return 0.125F;
        }
        return 0.25F;
    }

    // 游戏读取数据
    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.refreshDimensions();
    }
    //#endregion

    //#region 控制逻辑和移动逻辑
    // 移动逻辑(tick)
    @Override
    public void tick() {
        super.tick();

        boolean isBabyNow = this.isBaby();
        if (isBabyNow != this.wasBaby) {
            this.refreshDimensions(); // 刷新尺寸
            this.wasBaby = isBabyNow; // 更新状态
        }

        // 控制逻辑
        if (!this.isTame()) {
            findPlayerAndCheckAttractionConditions();

            if (attractedPlayer != null) {
                applyUntamedCreatureAttraction();
                // 使实体朝向玩家
                this.getLookControl().setLookAt(attractedPlayer, 10.0F, this.getMaxHeadXRot());
            } else {
                restrictUntamedEntityFlight();
            }
        }
        // else if (this.getOwner() instanceof Player owner) {
        //     handleTamedEntityFollowing(owner);
        // }

        // 动画逻辑
        if (this.level().isClientSide) {
            boolean isMovingHorizontally = 
                Math.abs(this.getDeltaMovement().x) > 0.001 || 
                Math.abs(this.getDeltaMovement().z) > 0.001;
            if (isMovingHorizontally && !this.moveAnimationState.isStarted()) {
                this.moveAnimationState.start(tickCount);
            }

            if (jumpCooldown > 0) {
                jumpCooldown--;
            } else if (isMovingHorizontally && this.random.nextFloat() < JUMP_CHANCE) {
                this.jumpAnimationState.start(tickCount);
                jumpCooldown = this.random.nextInt(JUMP_COOLDOWN_MAX - JUMP_COOLDOWN_MIN) + JUMP_COOLDOWN_MIN;
            }
        }
    }

    // 未驯服实体飞行限制
    private void restrictUntamedEntityFlight() {
        // 未被驯服时保持在至少60格高度
        int seaLevel = this.level().getSeaLevel(); // 获取海平面高度
        double currentY = this.getY();
        double targetMinY = seaLevel + MIN_FLYING_HEIGHT;
        double targetMaxY = seaLevel + MAX_FLYING_HEIGHT;
        double randomX_Y = (random.nextFloat()*0.2-0.1);

        if (currentY < targetMinY) {
            Vec3 motion = this.getDeltaMovement();
            double upwardSpeed = this.isBaby() ? 0.1 : 0.2;
            this.setDeltaMovement(motion.x+randomX_Y, upwardSpeed, motion.z+randomX_Y); // 向上移动
        } else if (currentY > targetMaxY) {
            // 高于最大高度，向下移动
            Vec3 motion = this.getDeltaMovement();
            double downwardSpeed = this.isBaby() ? -0.1 : -0.2;
            this.setDeltaMovement(motion.x+randomX_Y, downwardSpeed, motion.z+randomX_Y);
        }else {
            // 保持水平飞行，避免不必要的垂直移动
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, 0, motion.z);
        }
    }

    // // 驯服实体跟随逻辑
    // private void handleTamedEntityFollowing(Player owner) {
    //     // 被驯服后跟随
    //     double targetY = owner.getY() + 3;
    //     double currentY = this.getY();
    //     double heightDiff = currentY - targetY; // 高度差
    // 
    //     // 垂直方向的调整
    //     if (Math.abs(heightDiff) > 2.0F) {
    //         Vec3 motion = this.getDeltaMovement();
    //         double yAdjustmentSpeed = this.isBaby() ? 0.1 : 0.15;
    //         double yAdjustment = heightDiff > 0F ? -yAdjustmentSpeed : yAdjustmentSpeed;
    //         this.setDeltaMovement(motion.x, yAdjustment, motion.z);
    //     } else {
    //         // 保持当前高度
    //         Vec3 motion = this.getDeltaMovement();
    //         this.setDeltaMovement(motion.x, 0, motion.z);
    //     }
    // }

    // 未驯服生物能否被吸引判断
    private void findPlayerAndCheckAttractionConditions(){
        // 寻找玩家,并判断是否满足吸引条件(拿着发光浆果)
        Player nearestPlayer = this.level().getNearestPlayer(this, 16.0);
        if (nearestPlayer != null && nearestPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.GLOW_BERRIES) {
            if (randomFlyingGoal != null && this.goalSelector.getAvailableGoals().stream()
                    .anyMatch(w -> w.getGoal() == randomFlyingGoal)) {
                this.goalSelector.removeGoal(randomFlyingGoal);
            }
            
            attractedPlayer = nearestPlayer;
        } else {
            if (randomFlyingGoal != null && this.goalSelector.getAvailableGoals().stream()
                    .noneMatch(w -> w.getGoal() == randomFlyingGoal)) {
                this.goalSelector.addGoal(3, randomFlyingGoal);
            }

            attractedPlayer = null;
        }
    }

    // 未驯服生物被吸引逻辑
    private void applyUntamedCreatureAttraction() {
        Vec3 playerPos = attractedPlayer.position();
        Vec3 myPos = this.position();
        double dx = playerPos.x - myPos.x;
        double dy = (playerPos.y + 2) - myPos.y; // 目标是在玩家上方4格
        double dz = playerPos.z - myPos.z;
        
        double distance = Math.sqrt(dx * dx + dz * dz);
        double speed = this.isBaby() ? 0.1 : 0.15;
        
        if (distance > 3.0) { // 只在距离较远时移动
            this.setDeltaMovement(
                dx * 0.2 * speed,
                dy * 0.2 * speed,
                dz * 0.2 * speed
            );
        } else {
            // 靠近玩家时减缓速度
            this.setDeltaMovement(
                this.getDeltaMovement().x * 0.8,
                this.getDeltaMovement().y * 0.8,
                this.getDeltaMovement().z * 0.8
            );
        }
    }

    //#endregion

    //#region 实体生成相关逻辑
    // 被添加到世界时
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        // 确保实体被添加到世界后刷新尺寸
        this.refreshDimensions();
        
        // 如果是幼年实体，给它一个初始的向上速度
        if (this.isBaby()) {
            this.setDeltaMovement(0, 0.1, 0);
        }
    }

    // 移除时
    @Override
    public void remove(@Nonnull RemovalReason reason) {
        super.remove(reason);
        if (this.level().isClientSide) {
            this.moveAnimationState.stop();
            this.jumpAnimationState.stop();
        }
    }

    // 是否为baby
    @Override
    public boolean isBaby() {
        return super.isBaby();
    }

    // 设置baby
    @Override
    public void setBaby(boolean baby) {
        super.setBaby(baby);
        this.refreshDimensions();
    }

    // 通过繁殖生成时
    @Override
    @Nullable
    public AgeableMob getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
        CloudWhaleEntity baby = RegistryEntity.CLOUD_WHALE.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
            baby.setTame(true);
            baby.setOwnerUUID(this.getOwnerUUID());
        }
        
        
        return baby;
    }

    //#endregion

    // 添加驯服方法
    @Override
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!this.isTame() && itemstack.getItem() == Items.GLOW_BERRIES) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            
            // 10% 概率驯服
            if (this.random.nextInt(9) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte)7);
                if (
                    randomFlyingGoal != null && 
                    this.goalSelector.getAvailableGoals()
                        .stream().noneMatch(w -> w.getGoal() == randomFlyingGoal)
                ) {
                    this.goalSelector.addGoal(3, randomFlyingGoal);
                }
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        
        if (this.isTame() && this.isOwnedBy(player)) {
            
            return super.mobInteract(player, hand);
        }
        
        return super.mobInteract(player, hand);
    }


    // 注册动作
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        // this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 0.8D, 5.0F, 1.5F, true));
        this.randomFlyingGoal = new WaterAvoidingRandomFlyingGoal(this, 0.25D);
        this.goalSelector.addGoal(3, randomFlyingGoal);
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

}
