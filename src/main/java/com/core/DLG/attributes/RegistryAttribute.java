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
    // 暴击率
    public static final RegistryObject<Attribute> CRITICAL_CHANCE = ATTRIBUTES.register(
        "critical_chance", 
        () -> new RangedAttribute("attribute.dlg.critical_chance", playerDefaultCriticalChance, 0.0D, 1.0D).setSyncable(true)
    );
    // 暴击伤害
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTES.register(
        "critical_damage", 
        () -> new RangedAttribute("attribute.dlg.critical_damage", playerDefaultCriticalDamage, 0.0D, 512.0D).setSyncable(true)
    );
    // 闪避率
    public static final RegistryObject<Attribute> DODGE = ATTRIBUTES.register(
        "dodge", 
        () -> new RangedAttribute("attribute.dlg.dodge", 0.03D, 0.0D, 1.0D).setSyncable(true)
    );
    // 穿透率
    public static final RegistryObject<Attribute> PENETRATION_CHANCE = ATTRIBUTES.register(
        "penetration_chance", 
        () -> new RangedAttribute("attribute.dlg.penetration_chance", 0.0D, 0.0D, 1.0D).setSyncable(true)
    );
    // 穿透伤害
    public static final RegistryObject<Attribute> PENETRATION_DAMAGE = ATTRIBUTES.register(
        "penetration_damage", 
        () -> new RangedAttribute("attribute.dlg.penetration_damage", 0.0D, 0.0D, 512.0D).setSyncable(true)
    );
    // 吸血率
    public static final RegistryObject<Attribute> LIFESTEAL_CHANCE = ATTRIBUTES.register(
        "lifesteal_chance", 
        () -> new RangedAttribute("attribute.dlg.lifesteal_chance", 0.0D, 0.0D, 1.0D).setSyncable(true)
    );
    // 吸血伤害
    public static final RegistryObject<Attribute> LIFESTEAL_DAMAGE = ATTRIBUTES.register(
        "lifesteal_damage", 
        () -> new RangedAttribute("attribute.dlg.lifesteal_damage", 0.0D, 0.0D, 512.0D).setSyncable(true)
    );
    // 生命恢复
    public static final RegistryObject<Attribute> HP_REGEN = ATTRIBUTES.register(
        "hp_regen", 
        () -> new RangedAttribute("attribute.dlg.hp_regen", 0.0D, 0.0D, 512.0D).setSyncable(true)
    );
    // 治疗加成
    public static final RegistryObject<Attribute> HEALING_BONUS = ATTRIBUTES.register(
        "healing_bonus", 
        () -> new RangedAttribute("attribute.dlg.healing_bonus", 0.0D, 0.0D, 512.0D).setSyncable(true)
    );
}
