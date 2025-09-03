package com.core.DLG.attributes;

import java.io.IOException;

import com.core.DLG.DLG;
import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryAttribute {
    static {
        try {
            ItemConfig.detectConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Double playerDefaultCriticalChance = ItemConfig.getPlayerDefaultCriticalChance();
    public static final Double playerDefaultCriticalDamage = ItemConfig.getPlayerDefaultCriticalDamage();
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, DLG.MODID);
    public static final RegistryObject<Attribute> CRITICAL_CHANCE = ATTRIBUTES.register(
        "critical_chance", 
        () -> new RangedAttribute("attribute.dlg.critical_chance", playerDefaultCriticalChance, 0.0D, 1.0D).setSyncable(true)
    );
    
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTES.register(
        "critical_damage", 
        () -> new RangedAttribute("attribute.dlg.critical_damage", playerDefaultCriticalDamage, 0.0D, 512.0D).setSyncable(true)
    );
}
