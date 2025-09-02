package com.core.DLG.attributes;

import com.core.DLG.DLG;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryAttribute {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, DLG.MODID);
    public static final RegistryObject<Attribute> CRITICAL_CHANCE = ATTRIBUTES.register(
        "critical_chance", 
        () -> new RangedAttribute("attribute.dlg.critical_chance", 0.05D, 0.0D, 1.0D).setSyncable(true)
    );
    
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTES.register(
        "critical_damage", 
        () -> new RangedAttribute("attribute.dlg.critical_damage", 0.5D, 0.0D, 512.0D).setSyncable(true)
    );
}
