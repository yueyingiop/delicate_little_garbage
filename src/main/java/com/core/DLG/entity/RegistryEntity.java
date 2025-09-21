package com.core.DLG.entity;

import com.core.DLG.DLG;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryEntity {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DLG.MODID);

    public static final RegistryObject<EntityType<CloudWhaleEntity>> CLOUD_WHALE = ENTITIES.register(
        "cloud_whale",
        () -> EntityType.Builder.of(
            CloudWhaleEntity::new, 
            MobCategory.CREATURE
        )
            .sized(1.0f, 0.75f)
            .build("cloud_whale")
    );
}
