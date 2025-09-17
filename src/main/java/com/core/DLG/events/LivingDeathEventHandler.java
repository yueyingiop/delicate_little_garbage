package com.core.DLG.events;

import java.util.Random;

import com.core.DLG.DLG;
import com.core.DLG.item.RegistryItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingDeathEventHandler {
    private static final Random RANDOM = new Random();

    private static final float BASE_DROP_CHANCE = 0.2f; // 20%的掉落概率
    private static final float HEALTH_THRESHOLD = 20.0f; // 生命阈值
    private static final float HEALTH_PROBABILITY_FACTOR = 0.01f; // 生命概率因子
    

    private static final float HEALTH_PER_CRYSTAL = 10.0f; // 多少生命可以基于多少生命水晶
    private static final int RANGE_COUNT = 3; // 随机范围
    private static final int MIN_DROP = 1; // 最小掉落数量
    private static final int MAX_DROP = 10; // 最大掉落数量

    private static final int MIN_BOSS_DROP = 1; // BOSS最小额外掉落数量
    private static final int MAX_BOSS_DROP = 5; // BOSS最大额外掉落数量

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntity(); // 死亡的实体
            Entity killer = event.getSource().getEntity(); // 击杀者

            if (!entity.level().isClientSide && killer instanceof Player && entity instanceof Monster) {
                float maxHealth = entity.getMaxHealth();
                
                float healthProbabilityModifier;
                healthProbabilityModifier = (maxHealth < HEALTH_THRESHOLD) ?
                                            -(HEALTH_THRESHOLD - maxHealth) * HEALTH_PROBABILITY_FACTOR :
                                            (maxHealth - HEALTH_THRESHOLD) * HEALTH_PROBABILITY_FACTOR;
                float totalDropChance = BASE_DROP_CHANCE + healthProbabilityModifier;
                totalDropChance = Math.max(0.01f, Math.min(0.7f, totalDropChance));

                if (RANDOM.nextFloat() < totalDropChance) {
                    int dropCount = (int) (maxHealth / HEALTH_PER_CRYSTAL);
                    dropCount = dropCount + (RANGE_COUNT * (RANDOM.nextBoolean()? 1 : -1));
                    int count = Math.max(MIN_DROP, Math.min(MAX_DROP, dropCount));

                    ItemStack itemStack = new ItemStack(RegistryItem.LIFT_CRYSTAL.get(), count);
                    entity.spawnAtLocation(itemStack);
                }

                TagKey<EntityType<?>> bossTag = TagKey.create(
                    Registries.ENTITY_TYPE, 
                    ResourceLocation.fromNamespaceAndPath("forge", "bosses")
                );
                if (entity.getType().is(bossTag)) {
                    int bossDropCount = RANDOM.nextInt(MAX_BOSS_DROP - MIN_BOSS_DROP + 1) + MIN_BOSS_DROP;
                    ItemStack bossItemStack = new ItemStack(RegistryItem.DELICATE_LITTLE_GARBAGE.get(), bossDropCount);
                    entity.spawnAtLocation(bossItemStack);
                }
            }
        }
    }
}
