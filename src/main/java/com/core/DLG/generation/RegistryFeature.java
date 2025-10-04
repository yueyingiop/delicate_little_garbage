package com.core.DLG.generation;

import com.core.DLG.DLG;
import com.core.DLG.generation.world.CloudFeature;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DLG.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CLOUD_FEATURE = FEATURES.register(
        "cloud_feature",
        () -> new CloudFeature()
    );
}
