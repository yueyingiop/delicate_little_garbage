package com.core.DLG.item;

import com.core.DLG.DLG;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryItem extends Item {
    public RegistryItem(Properties properties) {
        super(properties);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DLG.MODID);

    // public static final RegistryObject<Item> PHONE = ITEMS.register(
    //     "phone", 
    //     () -> new RegistryItem(new Item.Properties())
    // );

    public static final RegistryObject<Item> EQUIPMENT_DEBRIS = ITEMS.register(
        "equipment_debris", 
        () -> new DebrisItem("equipment_debris", new Item.Properties())
    );
}
