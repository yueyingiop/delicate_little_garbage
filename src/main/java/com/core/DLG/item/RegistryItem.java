package com.core.DLG.item;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryItem extends Item {
    public RegistryItem(Properties properties) {
        super(properties);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DLG.MODID);

    public static final RegistryObject<Item> EQUIPMENT_DEBRIS = ITEMS.register(
        "equipment_debris", 
        () -> new DebrisItem("equipment_debris", new Item.Properties())
    );

    public static final RegistryObject<Item> DELICATE_LITTLE_GARBAGE = ITEMS.register(
        "delicate_little_garbage", 
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> LIFT_CRYSTAL = ITEMS.register(
        "life_crystal", 
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> INDESTRUCTIBLE_SCROLL = ITEMS.register(
        "indestructible_scroll",
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> NEPETA_CATARIA_ITEM = ITEMS.register(
        "nepeta_cataria_item",
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> NEPETA_CATARIA_SEEDS = ITEMS.register(
        "nepeta_cataria_seeds",
        () -> new ItemNameBlockItem(
            RegistryBlock.NEPETA_CATARIA.get(),
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> DEBRIS_SMITHING_TABLE_ITEM = ITEMS.register(
        "debris_smithing_table_item",
        () -> new CraftingBlockItem(
            "debris_smithing_table_item",
            RegistryBlock.DEBRIS_SMITHING_TABLE.get(),
            new Item.Properties()
        )
    );
}
