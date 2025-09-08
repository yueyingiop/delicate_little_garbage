package com.core.DLG.block.entity;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DLG.MODID);

    public static final RegistryObject<BlockEntityType<CraftingBlockEntity>> DEBRIS_SMITHING_TABLE_ENTITY = 
        BLOCK_ENTITIES.register(
            "debris_smithing_table",
            () -> BlockEntityType.Builder.of(
                CraftingBlockEntity::new,
                RegistryBlock.DEBRIS_SMITHING_TABLE.get()
            ).build(null)
        );
}
