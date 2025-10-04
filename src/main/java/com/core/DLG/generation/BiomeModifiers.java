package com.core.DLG.generation;

import com.core.DLG.DLG;
import com.core.DLG.generation.world.CloudGeneration;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

// 生物群系修改器
public class BiomeModifiers {
    // 生物群系修改器资源键(云方块)
    public static final ResourceKey<BiomeModifier> ADD_CLOUD_BLOCK = ResourceKey.create(
        ForgeRegistries.Keys.BIOME_MODIFIERS, // 生物群系修改器注册表
        ResourceLocation.fromNamespaceAndPath(DLG.MODID, "add_cloud_feature") // 资源路径
    );


    // 生物群系修改器注册
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE); // 放置特征注册表
        var biomes = context.lookup(Registries.BIOME); // 生物群系注册表

        // 注册生物群系修改器
        context.register(
            ADD_CLOUD_BLOCK,
            new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), // 所有主世界生物群系
                HolderSet.direct(placedFeatures.getOrThrow(CloudGeneration.PLACED_CLOUD_BLOCK_KEY)),
                GenerationStep.Decoration.RAW_GENERATION  // 生成阶段
            )
        );
    }
}
