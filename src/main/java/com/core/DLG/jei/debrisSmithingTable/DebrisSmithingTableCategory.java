package com.core.DLG.jei.debrisSmithingTable;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class DebrisSmithingTableCategory implements IRecipeCategory<DebrisSmithingTableRecipe> {
    public static final RecipeType<DebrisSmithingTableRecipe> TYPE = 
        RecipeType.create(DLG.MODID, "debris_smithing", DebrisSmithingTableRecipe.class);

    private static final ResourceLocation BACKGROUND_TEXTURE = 
        ResourceLocation.fromNamespaceAndPath(DLG.MODID, "textures/gui/jei/debris_smithing_table_background.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public DebrisSmithingTableCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND_TEXTURE, 0, 0, 170, 60)
            .setTextureSize(170, 60) // 纹理的实际尺寸
            .build();
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK, 
            new ItemStack(RegistryBlock.DEBRIS_SMITHING_TABLE.get())
        );
        this.title = Component.translatable("block.delicate_little_garbage.debris_smithing_table");
    }

    @Override
    public @NotNull RecipeType<DebrisSmithingTableRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
       return title;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(
        @Nonnull DebrisSmithingTableRecipe recipe, 
        @Nonnull IRecipeSlotsView recipeSlotsView, 
        @Nonnull GuiGraphics guiGraphics,
        double mouseX, 
        double mouseY
    ) {
        // 绘制配方类型标题
        MutableComponent typeTitle = getRecipeTypeTitle(recipe.getRecipeType());
        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(typeTitle);
        int x = (background.getWidth() - stringWidth) / 2;
        guiGraphics.drawString(font, typeTitle, x, 5, 0x404040, false);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull DebrisSmithingTableRecipe recipe, @Nonnull IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();
        
        switch (recipe.getRecipeType()) {
            case "bind" -> setupBindRecipe(builder, ingredients, recipe);
            case "upgrade_quality" -> setupUpgradeQualityRecipe(builder, ingredients, recipe);
            case "upgrade_level" -> setupUpgradeLevelRecipe(builder, ingredients, recipe);
            case "switch_slot" -> setupSwitchSlotRecipe(builder, ingredients, recipe);
        }
    }

    private MutableComponent getRecipeTypeTitle(String recipeType) {
        return switch (recipeType) {
            case "bind" -> Component.translatable("jei.delicate_little_garbage.recipe_type.bind");
            case "upgrade_quality" -> Component.translatable("jei.delicate_little_garbage.recipe_type.upgrade_quality");
            case "upgrade_level" -> Component.translatable("jei.delicate_little_garbage.recipe_type.upgrade_level");
            case "switch_slot" -> Component.translatable("jei.delicate_little_garbage.recipe_type.switch_slot");
            default -> Component.translatable("jei.delicate_little_garbage.recipe_type.unknown");
        };
    }

    private void setupBindRecipe(IRecipeLayoutBuilder builder, NonNullList<Ingredient> ingredients, DebrisSmithingTableRecipe recipe) {
        // 输入槽位：物品 + 碎片
        if (ingredients.size() >= 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 21).addIngredients(ingredients.get(0));
        }
        if (ingredients.size() >= 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 45, 21).addIngredients(ingredients.get(1));
        }
        // 输出槽位
        builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 21).addItemStack(recipe.getResultItem(null));
        
        // 添加描述
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
            .addItemStack(new ItemStack(RegistryBlock.DEBRIS_SMITHING_TABLE.get()));
    }

    private void setupUpgradeQualityRecipe(IRecipeLayoutBuilder builder, NonNullList<Ingredient> ingredients, DebrisSmithingTableRecipe recipe) {
        // 输入槽位：碎片 + 升级材料
        if (ingredients.size() >= 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 45, 21).addIngredients(ingredients.get(0));
        }
        if (ingredients.size() >= 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 63, 21).addIngredients(ingredients.get(1));
        }
        // 输出槽位
        builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 21).addItemStack(recipe.getResultItem(null));

        // 添加描述
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
            .addItemStack(new ItemStack(RegistryBlock.DEBRIS_SMITHING_TABLE.get()));
    }

    private void setupUpgradeLevelRecipe(IRecipeLayoutBuilder builder, NonNullList<Ingredient> ingredients, DebrisSmithingTableRecipe recipe) {
        // 输入槽位：绑定碎片的物品 + 经验材料
        if (ingredients.size() >= 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 21).addIngredients(ingredients.get(0));
        }
        if (ingredients.size() >= 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 63, 21).addIngredients(ingredients.get(1));
        }
        // 输出槽位
        builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 21).addItemStack(recipe.getResultItem(null));

        // 添加描述
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
            .addItemStack(new ItemStack(RegistryBlock.DEBRIS_SMITHING_TABLE.get()));
    }

    private void setupSwitchSlotRecipe(IRecipeLayoutBuilder builder, NonNullList<Ingredient> ingredients, DebrisSmithingTableRecipe recipe) {
        // 输入槽位：绑定碎片的物品 + 槽位材料
        if (ingredients.size() >= 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 21).addIngredients(ingredients.get(0));
        }
        if (ingredients.size() >= 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 45, 21).addIngredients(ingredients.get(1));
        }
        // 输出槽位
        builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 21).addItemStack(recipe.getResultItem(null));

        // 添加描述
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
            .addItemStack(new ItemStack(RegistryBlock.DEBRIS_SMITHING_TABLE.get()));
    }
}
