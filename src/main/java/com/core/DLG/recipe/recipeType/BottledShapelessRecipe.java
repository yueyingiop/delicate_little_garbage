package com.core.DLG.recipe.recipeType;

import javax.annotation.Nonnull;

import com.core.DLG.recipe.RegistryRecipe;
import com.core.DLG.util.ModTags;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class BottledShapelessRecipe extends ShapelessRecipe {

    public BottledShapelessRecipe(
        ResourceLocation id, 
        String group, 
        CraftingBookCategory category,
        ItemStack result, 
        NonNullList<Ingredient> ingredients
    ) {
        super(id, group, category, result, ingredients);
    }

    // 获取剩余物品
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer container) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(container); // 获取剩余物品
        for (int i = 0; i < remaining.size(); i++) {
            ItemStack stack = container.getItem(i); // 获取槽位中的物品
            if (!stack.isEmpty() && stack.is(ModTags.Items.BOTTLED_INGREDIENTS)) {
               remaining.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryRecipe.BOTTLED_SHAPELESS_SERIALIZER.get();
    }
}
