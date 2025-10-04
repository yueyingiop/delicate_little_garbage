package com.core.DLG.generation.world;

import java.util.List;

import com.core.DLG.DLG;
import com.core.DLG.generation.RegistryFeature;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

// 配置类
public class CloudGeneration {
    // 配置特征的资源键
    public static final ResourceKey<ConfiguredFeature<?, ?>> CLOUD_BLOCK_KEY = ResourceKey.create(
        Registries.CONFIGURED_FEATURE, // 配置特征注册表
        ResourceLocation.fromNamespaceAndPath(DLG.MODID, "cloud_block") // 资源路径
    );

    // 放置特征的资源键
    public static final ResourceKey<PlacedFeature> PLACED_CLOUD_BLOCK_KEY = ResourceKey.create(
        Registries.PLACED_FEATURE, // 放置特征注册表
        ResourceLocation.fromNamespaceAndPath(DLG.MODID, "cloud_block_placed")
    );

    // 配置特征
    public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        
        // 注册配置特征
        context.register(
            CLOUD_BLOCK_KEY, // 配置特征资源键
            new ConfiguredFeature<>( // 配置特征实例
                RegistryFeature.CLOUD_FEATURE.get(), // 特征
                new NoneFeatureConfiguration() // 特征配置(无使用配置)
            )
        );
    }

    // 配置放置特征
    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        // 获取放置特征
        Holder<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CLOUD_BLOCK_KEY);
        
        // 注册放置特征
        context.register(
            PLACED_CLOUD_BLOCK_KEY, // 放置特征资源键
            new PlacedFeature( // 放置特征实例
                configuredFeature, // 配置特征
                List.of(
                    // 放置高度范围
                    HeightRangePlacement.uniform(
                        VerticalAnchor.absolute(190),
                        VerticalAnchor.absolute(220)
                    ),
                    // 放置次数
                    CountPlacement.of(3),
                    // 放置位置(在表面)
                    InSquarePlacement.spread(),
                    // 放置过滤器(在生物群系中)
                    BiomeFilter.biome()
                )
            )
        );
    }
}
