package com.core.DLG.jei;

import java.util.List;

import com.core.DLG.item.RegistryItem;

import net.minecraft.world.item.ItemStack;

public class RegistryInfoDisplay {
    // JeiInfoDisplay两种用法
    // 1.类方法
    // InfoData infoData = new InfoData(
    //     RegistryItem.EQUIPMENT_DEBRIS.get().getDefaultInstance(), 
    //     true, 
    //     List.of(
    //         new InfoGroup(
    //             RegistryItem.EQUIPMENT_DEBRIS.get().getDefaultInstance(),
    //             "get", 
    //             Style.EMPTY.withColor(ChatFormatting.GOLD)
    //         )
    //         .addInfo(List.of(
    //             Style.EMPTY,
    //             Style.EMPTY,
    //             Style.EMPTY
    //         )),
    //         new InfoGroup(
    //             RegistryItem.EQUIPMENT_DEBRIS.get().getDefaultInstance(),
    //             "use", 
    //             Style.EMPTY.withColor(ChatFormatting.GOLD)
    //         )
    //         .addInfo(List.of(
    //             Style.EMPTY,
    //             Style.EMPTY,
    //             Style.EMPTY
    //         ))
    //     )
    // );
    // JeiInfoDisplay jeiInfoDisplay = JeiInfoDisplay.buildDescription(infoData);
    // registration.addItemStackInfo(
    //     new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get()),
    //     jeiInfoDisplay
    // );
    // 
    // 
    // 2.实例方法
    // JeiInfoDisplay jeiInfoDisplay = new JeiInfoDisplay(
    //     new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get()),
    //     List.of("get","use"),
    //     List.of(
    //         Style.EMPTY.withColor(ChatFormatting.GOLD),
    //         Style.EMPTY.withColor(ChatFormatting.GOLD)
    //     ),
    //     List.of(
    //         List.of(
    //             Style.EMPTY,
    //             Style.EMPTY,
    //             Style.EMPTY
    //         ),
    //         List.of(
    //             Style.EMPTY,
    //             Style.EMPTY,
    //             Style.EMPTY
    //         )
    //     )
    // );
    // registration.addItemStackInfo(
    //     new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get()),
    //     jeiInfoDisplay
    // );


    public static JeiInfoDisplay equipmentDebrisInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get()),
        List.of("get","use")
    );

    public static JeiInfoDisplay cloudBlockInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.CLOUD_BLOCK_ITEM.get()),
        List.of("get")
    );

    public static JeiInfoDisplay cloudBottleInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.CLOUD_BOTTLE.get()),
        List.of("get","use")
    );

    public static JeiInfoDisplay indestructibleScrollInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.INDESTRUCTIBLE_SCROLL.get()),
        List.of("get","use")
    );

    public static JeiInfoDisplay delicateLittleGarbageInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.DELICATE_LITTLE_GARBAGE.get()),
        List.of("get","use")
    );

    public static JeiInfoDisplay lifeCrystalInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.LIFT_CRYSTAL.get()),
        List.of("get","use")
    );

    public static JeiInfoDisplay nepetaCatariaItemInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.NEPETA_CATARIA_ITEM.get()),
        List.of("comments")
    );

    public static JeiInfoDisplay nepetaCatariaSeedsInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.NEPETA_CATARIA_SEEDS.get()),
        List.of("get","comments")
    );

    public static JeiInfoDisplay cloudSugarInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.CLOUD_SUGAR.get()),
        List.of("use")
    );

    public static JeiInfoDisplay cloudWhaleInfoDisplay = new JeiInfoDisplay(
        new ItemStack(RegistryItem.CLOUD_WHALE_SPAWN_EGG.get()),
        List.of("comments")
    );
}
