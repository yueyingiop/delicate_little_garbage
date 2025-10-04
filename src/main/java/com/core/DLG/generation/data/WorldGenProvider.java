package com.core.DLG.generation.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.core.DLG.DLG;
import com.core.DLG.generation.BiomeModifiers;
import com.core.DLG.generation.world.CloudGeneration;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

// 世界生成提供器
public class WorldGenProvider extends DatapackBuiltinEntriesProvider {
    public WorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(output, registries, BUILDER, Set.of(DLG.MODID));
    }

    // 注册构建器实例
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, CloudGeneration::bootstrapConfigured) // 注册配置特征
        .add(Registries.PLACED_FEATURE, CloudGeneration::bootstrapPlaced) // 注册放置特征
        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstrap); // 注册生物群系修改器
    
}
