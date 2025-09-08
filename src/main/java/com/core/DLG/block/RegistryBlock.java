package com.core.DLG.block;

import com.core.DLG.DLG;

import net.minecraft.world.level.block.Block;
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
}
