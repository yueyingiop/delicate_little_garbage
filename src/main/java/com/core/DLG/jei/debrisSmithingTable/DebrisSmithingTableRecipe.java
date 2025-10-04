package com.core.DLG.jei.debrisSmithingTable;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class DebrisSmithingTableRecipe implements Recipe<Container> {
    private final ResourceLocation id; // 配方ID
    private final NonNullList<Ingredient> inputs; // 输入
    private final ItemStack output; // 输出
    private final String recipeType; // 配方类型

    public DebrisSmithingTableRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output, String recipeType) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.recipeType = recipeType;
    }

    @Override
    public boolean matches(@Nonnull Container container, @Nonnull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@Nonnull Container container, @Nonnull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nonnull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return null; // 不需要序列化
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return null; // 不需要注册类型
    }

    public String getRecipeType() {
        return recipeType;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

}
