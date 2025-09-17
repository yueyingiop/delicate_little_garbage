package com.core.DLG.effect;

import com.core.DLG.DLG;
import com.core.DLG.item.RegistryItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryBrewingRecipe {
    @SubscribeEvent
    public static void registerBrewingRecipes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 好猫药水基础版
            addBrewingRecipe(
                // 粗制药水
                Potions.AWKWARD, 
                RegistryItem.NEPETA_CATARIA_ITEM.get(), 
                RegistryPotion.CAT_POTION.get()
            );
        });
    }

    // 基础酿造配方
    private static void addBrewingRecipe(Potion inputPotion, Item ingredient, Potion outputPotion) {
        ItemStack input = PotionUtils.setPotion(new ItemStack(Items.POTION), inputPotion);
        ItemStack output = PotionUtils.setPotion(new ItemStack(Items.POTION), outputPotion);
    
        BrewingRecipeRegistry.addRecipe(
            Ingredient.of(input),
            Ingredient.of(ingredient),
            output
        );
    }

    // 喷溅/滞留型药水配方
    private static void addBrewingRecipe(Potion inputPotion, Item ingredient, Potion outputPotion, boolean isSplash) {
        ItemStack input = PotionUtils.setPotion(new ItemStack(isSplash ? Items.POTION : Items.SPLASH_POTION), inputPotion);
        ItemStack output = PotionUtils.setPotion(new ItemStack(isSplash ? Items.SPLASH_POTION : Items.LINGERING_POTION), inputPotion);
        BrewingRecipeRegistry.addRecipe(
            Ingredient.of(input),
            Ingredient.of(ingredient),
            output
        );
    }
}
