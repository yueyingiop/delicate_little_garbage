package com.core.DLG.mixin.player.food;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.core.DLG.configs.PlayerConfig;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @Shadow
    private int foodLevel; // 饥饿值
    @Shadow
    private float saturationLevel; // 当前饱和度
    @Shadow
    private float exhaustionLevel; // 饱和度消耗水平
    @Shadow
    private int tickTimer;//计时器，对应游戏刻(一般20刻为一秒)
    @Shadow
    private int lastFoodLevel;//前一刻饥饿水平

    @Inject(method = "<init>",at = @At("TAIL")) // 构造函数注入
    public void init(CallbackInfo ci) throws IOException {
        PlayerConfig.init();
        this.foodLevel = PlayerConfig.getMaxHungry();
    }

    /**
     * @author yueyingiop
     * @reason 修改最大饥饿值
    */
    @Overwrite
    public void eat(int foodLevelIn, float foodSaturationModifier) throws IOException {
        PlayerConfig.init();
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, PlayerConfig.getMaxHungry());
        this.saturationLevel = Math.min(this.saturationLevel + (float) foodLevelIn * foodSaturationModifier * 2.0F, (float) this.foodLevel);
    }

    /**
     * @author yueyingiop
     * @reason 修改饥饿值消耗逻辑
    */
    @Overwrite
    public void tick(Player player) throws IOException {
        PlayerConfig.init();

        // 原版机制
        Difficulty difficulty = player.level().getDifficulty();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean flag = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        if (flag && this.saturationLevel > 0.0F && player.isHurt() && this.foodLevel >= PlayerConfig.getMaxHungry()) {
            ++this.tickTimer;
            if (this.tickTimer >= 10) {
                float f = Math.min(this.saturationLevel, 6.0F);
                player.heal(f / 6.0F);
                this.addExhaustion(f);
                this.tickTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && player.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                player.heal(1.0F);
                this.addExhaustion(6.0F);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {//如果饥饿度小于等于0
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                //如果玩家的生命值大于10.0，或者困难难度，或者生命值大于1.0且普通难度，执行以下操作。
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.hurt(player.damageSources().starve(), 1.0F);//以1.0的伤害值攻击玩家，模拟饥饿造成的伤害
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }

    /**
     * @author yueyingiop
     * @reason 修改最大饥饿值
    */
    @Overwrite
    public boolean needsFood() throws IOException {
        PlayerConfig.init();
        return foodLevel < PlayerConfig.getMaxHungry();
    }

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    /**
     * @author yueyingiop
     * @reason 修改最大饥饿值
    */
    @Overwrite
    public void setFoodLevel(int foodLevel) throws IOException {
        PlayerConfig.init();
        if (foodLevel > PlayerConfig.getMaxHungry()) return;
        this.lastFoodLevel = this.foodLevel;
        this.foodLevel = Math.min(foodLevel, PlayerConfig.getMaxHungry());
    }

    /**
     * @author yueyingiop
     * @reason 修改最大饥饿值
    */
    @Overwrite
    public void setSaturation(float saturation) {
        if (saturation > foodLevel * 1.0f) return;
        this.saturationLevel = Math.min(saturation, foodLevel);
    }

}
