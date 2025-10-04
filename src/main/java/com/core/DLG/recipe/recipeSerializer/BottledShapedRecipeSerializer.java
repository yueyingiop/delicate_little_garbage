package com.core.DLG.recipe.recipeSerializer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.core.DLG.recipe.recipeType.BottledShapedRecipe;
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

public class BottledShapedRecipeSerializer implements RecipeSerializer<BottledShapedRecipe> {

    @Override
    public BottledShapedRecipe fromJson(
        @Nonnull ResourceLocation recipeId, 
        @Nonnull JsonObject json
    ) {
        String group = GsonHelper.getAsString(json, "group", "");
        CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
        
        // 使用原版的有序合成解析逻辑
        ShapedRecipe tmp = ShapedRecipe.Serializer.SHAPED_RECIPE.fromJson(recipeId, json);
        return new BottledShapedRecipe(
            recipeId, 
            group, 
            category, 
            tmp.getRecipeWidth(), 
            tmp.getRecipeHeight(), 
            tmp.getIngredients(), 
            tmp.getResultItem(null)
        );
    }

    @Override
    public @Nullable BottledShapedRecipe fromNetwork(
        @Nonnull ResourceLocation recipeId, 
        @Nonnull FriendlyByteBuf buffer
    ) {
        int width = buffer.readVarInt();
        int height = buffer.readVarInt();
        String group = buffer.readUtf();
        CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
    
        int ingredientCount = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();
        return new BottledShapedRecipe(
            recipeId, 
            group, 
            category, 
            width, 
            height, 
            ingredients, 
            result
        );
    }

    @Override
    public void toNetwork(
        @Nonnull FriendlyByteBuf buffer, 
        @Nonnull BottledShapedRecipe recipe
    ) {
        buffer.writeVarInt(recipe.getWidth());
        buffer.writeVarInt(recipe.getHeight());
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }
        
        buffer.writeItem(recipe.getResultItem(null));
    }

}
