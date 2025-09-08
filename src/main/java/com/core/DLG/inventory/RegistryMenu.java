package com.core.DLG.inventory;

import com.core.DLG.DLG;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DLG.MODID);

    public static final RegistryObject<MenuType<CraftingBlockMenu>> CRAFTING_BLOCK_MENU = 
    MENUS.register(
        "crafting_block_menu", 
        () -> IForgeMenuType.create(CraftingBlockMenu::new)
    );
}
