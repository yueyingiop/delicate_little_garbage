package com.core.DLG.jei;

import javax.annotation.Nonnull;

import com.core.DLG.DLG;
import com.core.DLG.inventory.CraftingBlockScreen;
import com.core.DLG.item.RegistryItem;
import com.core.DLG.jei.debrisSmithingTable.DebrisSmithingTableCategory;
import com.core.DLG.jei.debrisSmithingTable.DebrisSmithingTableRecipeManager;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class DLGJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(DLG.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DebrisSmithingTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        registration.addRecipes(
            DebrisSmithingTableCategory.TYPE, 
            DebrisSmithingTableRecipeManager.getRecipes()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get()),
            RegistryInfoDisplay.equipmentDebrisInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.CLOUD_BLOCK_ITEM.get()),
            RegistryInfoDisplay.cloudBlockInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.CLOUD_BOTTLE.get()),
            RegistryInfoDisplay.cloudBottleInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.INDESTRUCTIBLE_SCROLL.get()),
            RegistryInfoDisplay.indestructibleScrollInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.DELICATE_LITTLE_GARBAGE.get()),
            RegistryInfoDisplay.delicateLittleGarbageInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.LIFT_CRYSTAL.get()),
            RegistryInfoDisplay.lifeCrystalInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.NEPETA_CATARIA_ITEM.get()),
            RegistryInfoDisplay.nepetaCatariaItemInfoDisplay.buildDescription()
        );

        registration.addItemStackInfo(
            new ItemStack(RegistryItem.NEPETA_CATARIA_SEEDS.get()),
            RegistryInfoDisplay.nepetaCatariaSeedsInfoDisplay.buildDescription()
        );
    }

    @Override
    public void registerGuiHandlers(@Nonnull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
            CraftingBlockScreen.class, 
            89, 33, 22, 22, 
            DebrisSmithingTableCategory.TYPE
        );
    }
}
