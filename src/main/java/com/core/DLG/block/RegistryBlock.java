package com.core.DLG.block;

import com.core.DLG.DLG;
import com.core.DLG.item.RegistryItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DLG.MODID);

    public static final RegistryObject<Block> DEBRIS_SMITHING_TABLE = 
    BLOCKS.register(
        "debris_smithing_table",
        () -> new CraftingBlock(
            "debris_smithing_table",
            BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(3.5F, 10.0F)
                .noOcclusion()
                .isViewBlocking((state, world, pos) -> false)
                .isSuffocating((state, world, pos) -> false)
                .lightLevel(state -> 2)
            ,
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D)
        )
    );

    public static final RegistryObject<Block> NEPETA_CATARIA = 
    BLOCKS.register(
        "nepeta_cataria",
        () -> new PlantsBlock(
            "nepeta_cataria",
            BlockBehaviour.Properties.copy(Blocks.WHEAT)
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP)
                .randomTicks(),
            () -> RegistryItem.NEPETA_CATARIA_SEEDS.get()
        )
    );
}
