package com.core.DLG.effect;

import com.core.DLG.DLG;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryPotion {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, DLG.MODID);

    public static final RegistryObject<Potion> CAT_POTION = POTIONS.register(
        "cat_potion", 
        () -> new Potion(
            new MobEffectInstance(
                RegistryEffect.CAT_EFFECT.get(),
                3600,
                0
            )
        )
    );
}
