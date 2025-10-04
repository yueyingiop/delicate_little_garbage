package com.core.DLG.recipe;

import com.core.DLG.DLG;
import com.core.DLG.recipe.recipeSerializer.BottledShapedRecipeSerializer;
import com.core.DLG.recipe.recipeSerializer.BottledShapelessRecipeSerializer;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryRecipe {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DLG.MODID);

    public static final RegistryObject<RecipeSerializer<?>> BOTTLED_SHAPED_SERIALIZER  = 
        RECIPE_SERIALIZERS.register(
            "custom_shaped",
            BottledShapedRecipeSerializer::new
        );
    public static final RegistryObject<RecipeSerializer<?>> BOTTLED_SHAPELESS_SERIALIZER = 
        RECIPE_SERIALIZERS.register(
            "custom_shapeless", 
            BottledShapelessRecipeSerializer::new
        );
}
