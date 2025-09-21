package com.core.DLG.loot;

import com.core.DLG.DLG;
import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryGLM {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM =
        DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DLG.MODID);

    public static final RegistryObject<Codec<EndCityScrollModifier>> END_CITY_SCROLL =
        GLM.register(
            "end_city_scroll", 
            () -> EndCityScrollModifier.CODEC
        );

}
