package com.core.DLG.recipe.recipeSerializer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.core.DLG.recipe.recipeType.BottledShapelessRecipe;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class BottledShapelessRecipeSerializer implements RecipeSerializer<BottledShapelessRecipe> {
    @Override
    public BottledShapelessRecipe fromJson(
        @Nonnull ResourceLocation recipeId, 
        @Nonnull JsonObject json
    ) {
        String group = GsonHelper.getAsString(json, "group", "");
        CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
        
        // 使用原版的有序合成解析逻辑
        ShapelessRecipe tmp = ShapedRecipe.Serializer.SHAPELESS_RECIPE.fromJson(recipeId, json);
        return new BottledShapelessRecipe(
            recipeId, 
            group, 
            category, 
            tmp.getResultItem(null),
            tmp.getIngredients()
        );
    }

    @Override
    public @Nullable BottledShapelessRecipe fromNetwork(
        @Nonnull ResourceLocation recipeId, 
        @Nonnull FriendlyByteBuf buffer
    ) {
        String group = buffer.readUtf();
        CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
    
        int ingredientCount = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();
        return new BottledShapelessRecipe(
            recipeId, 
            group, 
            category, 
            result,
            ingredients
        );
    }

    @Override
    public void toNetwork(
        @Nonnull FriendlyByteBuf buffer, 
        @Nonnull BottledShapelessRecipe recipe
    ) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        buffer.writeVarInt(recipe.getIngredients().size());
        
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }
        
        buffer.writeItem(recipe.getResultItem(null));
    }
}
