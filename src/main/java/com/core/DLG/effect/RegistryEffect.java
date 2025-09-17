package com.core.DLG.effect;

import com.core.DLG.DLG;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryEffect {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DLG.MODID);

    public static final RegistryObject<MobEffect> CAT_EFFECT = EFFECTS.register("cat_effect", () -> new CatEffect());
}
