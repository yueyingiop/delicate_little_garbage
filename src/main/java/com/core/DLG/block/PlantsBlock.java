package com.core.DLG.block;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlantsBlock extends CropBlock {
    private final VoxelShape[] SHAPE_BY_AGE;
    private final Supplier<Item> seedItem;
    private final String blockId;

    public PlantsBlock(String blockId, Properties properties, Supplier<Item> seedItem) {
        this(blockId, properties, seedItem, new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
        });
    }

    public PlantsBlock(String blockId,Properties properties, Supplier<Item> seedItem, VoxelShape[] shapeByAge) {
        super(properties);
        this.blockId = blockId;
        this.SHAPE_BY_AGE = shapeByAge;
        this.seedItem = seedItem;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seedItem.get();
    }

    @Override
    public VoxelShape getShape(
        @Nonnull BlockState state, 
        @Nonnull BlockGetter world, 
        @Nonnull BlockPos pos,
        @Nonnull CollisionContext context
    ) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        boolean hasSeed = false;
        Item seedItem = this.getBaseSeedId().asItem();

        for (ItemStack drop : drops) {
            if (drop.getItem() == seedItem) {
                hasSeed = true;
                break;
            }
        }

        if (!hasSeed) {
            ItemStack seedStack = new ItemStack(seedItem);
            drops.add(seedStack);
        }

        return drops;
    }

    @Override
    public void randomTick(
        @Nonnull BlockState state, 
        @Nonnull ServerLevel level, 
        @Nonnull BlockPos pos, 
        @Nonnull RandomSource random
    ) {
        super.randomTick(state, level, pos, random);
        
        // 当作物成熟时，设置一个定时任务来持续吸引猫
        if (this.getAge(state) >= this.getMaxAge() - 1) {
            level.getServer().execute(() -> {
                if (level.getBlockState(pos).getBlock() == this) {
                    attractCats(level, pos);
                }
            });
        }
    }

    // 猫薄荷的猫猫吸引
    public void attractCats(ServerLevel level, BlockPos pos) {
        // 定义搜索范围（10格半径）
        AABB searchArea = new AABB(pos).inflate(10.0D);
        
        // 查找范围内的猫
        List<Cat> cats = level.getEntitiesOfClass(Cat.class, searchArea);
        for (Cat cat : cats) {
            // 设置猫的目标位置为猫薄荷的位置
            cat.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0D);
        }
        
        // 查找范围内的豹猫
        List<Ocelot> ocelots = level.getEntitiesOfClass(Ocelot.class, searchArea);
        for (Ocelot ocelot : ocelots) {
            // 设置豹猫的目标位置为猫薄荷的位置
            ocelot.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0D);
        }
        level.scheduleTick(pos, this, 100);
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        // 当计划刻触发时，再次吸引猫
        if (this.getAge(state) == this.getMaxAge() && this.blockId.equals("nepeta_cataria")) {
            attractCats(level, pos);
        }
    }
}
