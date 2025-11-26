package com.core.DLG.item;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;
import com.core.DLG.entity.RegistryEntity;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
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

    // 注册碎片物品
    public static final RegistryObject<Item> EQUIPMENT_DEBRIS = ITEMS.register(
        "equipment_debris", 
        () -> new DebrisItem("equipment_debris", new Item.Properties())
    );

    // 注册普通物品
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

    public static final RegistryObject<Item> NEPETA_CATARIA_LEAF = ITEMS.register(
        "nepeta_cataria_leaf",
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CLOUD_BOTTLE = ITEMS.register(
        "cloud_bottle",
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> ANY = ITEMS.register(
        "any",
        () -> new RegistryItem(
            new Item.Properties()
        )
    );

    // 注册作物种子
    public static final RegistryObject<Item> NEPETA_CATARIA_SEEDS = ITEMS.register(
        "nepeta_cataria_seeds",
        () -> new ItemNameBlockItem(
            RegistryBlock.NEPETA_CATARIA.get(),
            new Item.Properties()
        )
    );

    // 注册食物
    public static final RegistryObject<Item> CLOUD_SUGAR = ITEMS.register(
        "cloud_sugar",
        () -> new RegistryItem(
            new Item.Properties().food(
                new FoodProperties.Builder()
                    .nutrition(6) // 饥饿值
                    .saturationMod(0.5F) // 饱和度
                    .build()
            )
        )
    );

    // 注册工作方块
    public static final RegistryObject<Item> DEBRIS_SMITHING_TABLE_ITEM = ITEMS.register(
        "debris_smithing_table_item",
        () -> new CraftingBlockItem(
            "debris_smithing_table_item",
            RegistryBlock.DEBRIS_SMITHING_TABLE.get(),
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CLOUD_WHALE_SPAWN_EGG = ITEMS.register(
        "cloud_whale_spawn_egg",
        () -> new CloudWhaleSpawnEggItem(
            RegistryEntity.CLOUD_WHALE, 
            0xEB8FCF,
            0xFFFFFF,
            new Item.Properties()
        )
    );

    public static final RegistryObject<Item> CLOUD_BLOCK_ITEM = ITEMS.register(
        "cloud_block_item",
        () -> new BlockItem(
            RegistryBlock.CLOUD_BLOCK.get(), 
            new Item.Properties()    
        )
    );

    public static final RegistryObject<Item> PORTABLE_WORKER_ITEM = ITEMS.register(
        "portable_worker_item",
        () -> new PortableWorkerItem(
            new Item.Properties()
        )
    );
}
