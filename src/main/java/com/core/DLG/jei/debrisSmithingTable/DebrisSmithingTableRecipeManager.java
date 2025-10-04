package com.core.DLG.jei.debrisSmithingTable;

import java.util.ArrayList;
import java.util.List;

import com.core.DLG.DLG;
import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.item.RegistryItem;
import com.core.DLG.util.EntryHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class DebrisSmithingTableRecipeManager {
    public static List<DebrisSmithingTableRecipe> getRecipes() {
        List<DebrisSmithingTableRecipe> recipes = new ArrayList<>();
        
        // 添加绑定词条配方示例
        recipes.addAll(createBindRecipes());
        // 添加提升品质配方示例
        recipes.addAll(createUpgradeQualityRecipes());
        // 添加升级等级配方示例
        recipes.addAll(createUpgradeLevelRecipes());
        // 添加更换槽位配方示例
        recipes.addAll(createSwitchSlotRecipes());
        
        return recipes;
    }
    
    // 创建绑定配方
    private static List<DebrisSmithingTableRecipe> createBindRecipes() {
        List<DebrisSmithingTableRecipe> recipes = new ArrayList<>();
        
        ItemStack AnyItem = new ItemStack(RegistryItem.ANY.get());
        ItemStack debris = createDebris("undefined_type", 9);
        ItemStack result = createBoundItem(AnyItem, "undefined_type", 9);
        
        NonNullList<Ingredient> inputs = NonNullList.create();
        inputs.add(Ingredient.of(AnyItem));
        inputs.add(Ingredient.of(debris));
        
        recipes.add(new DebrisSmithingTableRecipe(
            ResourceLocation.fromNamespaceAndPath(DLG.MODID, "bind_iron_sword"),
            inputs, result, "bind"
        ));
        
        return recipes;
    }
    
    // 创建碎片提升品质配方
    private static List<DebrisSmithingTableRecipe> createUpgradeQualityRecipes() {
        List<DebrisSmithingTableRecipe> recipes = new ArrayList<>();
        try {
            ItemConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JsonArray upgradeMaterials = ItemConfig.getupgradeMaterialsList();
        for (int i = 0; i < upgradeMaterials.size(); i++) {
            JsonObject upgradeMaterialObject = upgradeMaterials.get(i).getAsJsonObject();
            String nameString = upgradeMaterialObject.get("upgradeMaterial").getAsString();
            int quality = upgradeMaterialObject.get("quality").getAsInt();
            
            Ingredient upgradeMaterial = getIngredientFromString(nameString);
            ItemStack debris = createDebris("undefined_type", quality);
            ItemStack result = createDebris("undefined_type", quality+1);

            NonNullList<Ingredient> inputs = NonNullList.create();
            inputs.add(Ingredient.of(debris));
            inputs.add(upgradeMaterial);
            
            recipes.add(new DebrisSmithingTableRecipe(
                ResourceLocation.fromNamespaceAndPath(DLG.MODID, "upgrade_quality"),
                inputs, result, "upgrade_quality"
            ));
        }

        return recipes;
    }
    
    // 物品等级升级配方
    private static List<DebrisSmithingTableRecipe> createUpgradeLevelRecipes() {
        List<DebrisSmithingTableRecipe> recipes = new ArrayList<>();
        JsonArray expMaterials = ItemConfig.getExpMaterialsList();
        
        for (int i = 0; i < expMaterials.size(); i++) {
            JsonObject itemConfig = expMaterials.get(i).getAsJsonObject();
            String nameString = itemConfig.get("expMaterial").getAsString();
            int exp = itemConfig.get("exp").getAsInt();
            
            Ingredient expMaterial = getIngredientFromString(nameString);
            ItemStack boundItem = createBoundItem(new ItemStack(Items.IRON_SWORD), "sword", 3);
            ItemStack result = createUpgradedItem(boundItem, exp);

            NonNullList<Ingredient> inputs = NonNullList.create();
            inputs.add(Ingredient.of(boundItem));
            inputs.add(expMaterial);
            
            recipes.add(new DebrisSmithingTableRecipe(
                ResourceLocation.fromNamespaceAndPath(DLG.MODID, "upgrade_level"),
                inputs, result, "upgrade_level"
            ));
        }
        
        return recipes;
    }
    
    // 槽位更换配方
    private static List<DebrisSmithingTableRecipe> createSwitchSlotRecipes() {
        List<DebrisSmithingTableRecipe> recipes = new ArrayList<>();
        List<ItemStack> soltItemStacks = List.of(
            new ItemStack(Items.STICK),
            new ItemStack(Items.SPIDER_EYE),
            new ItemStack(Items.ROTTEN_FLESH),
            new ItemStack(Items.BONE),
            new ItemStack(Items.STRING)
        );

        for (int i = 0; i < soltItemStacks.size(); i++) {
            ItemStack boundItem = createBoundItem(new ItemStack(RegistryItem.ANY.get()), "undefined_type", 9);
            ItemStack slotMaterial = soltItemStacks.get(i);
            ItemStack result = createSlotChangedItem(boundItem, slotMaterial);

            NonNullList<Ingredient> inputs = NonNullList.create();
            inputs.add(Ingredient.of(boundItem));
            inputs.add(Ingredient.of(slotMaterial));

            recipes.add(new DebrisSmithingTableRecipe(
                ResourceLocation.fromNamespaceAndPath(DLG.MODID, "switch_slot"),
                inputs, result, "switch_slot"
            ));
        };

        return recipes;
    }
    
    // 工具方法：创建碎片
    private static ItemStack createDebris(String type, int quality) {
        ItemStack debris = new ItemStack(RegistryItem.EQUIPMENT_DEBRIS.get());
        var tag = debris.getOrCreateTag();
        tag.putString("type", type);
        tag.putInt("quality", quality);
        return debris;
    }
    
    // 工具方法：创建绑定碎片的物品
    private static ItemStack createBoundItem(ItemStack base, String type, int quality) {
        ItemStack result = base.copy();
        CompoundTag tag = result.getOrCreateTag();
        CompoundTag debrisTag = result.getOrCreateTagElement("boundDebris");
        debrisTag.putBoolean("bound", true);
        debrisTag.putString("type", type);
        debrisTag.putInt("quality", quality);
        debrisTag.putInt("level", 0);
        debrisTag.putInt("maxLevel", quality * 5);
        debrisTag.putInt("exp", 0);
        debrisTag.putInt("maxExp", 100);
        try {
            EntryHelper.initEntry(
                tag, 
                TypeEnum.getType(debrisTag.getString("type")), 
                QualityEnum.getQuality(debrisTag.getInt("quality"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntryHelper.transitDefaultAttribute(result, tag);
        return result;
    }
    
    // 工具方法：创建升级后的物品
    private static ItemStack createUpgradedItem(ItemStack base, int exp) {
        ItemStack result = base.copy();
        int newExp = exp; // 新经验值初始化
        int newLevel = 0; // 新等级初始化
        int maxExp = 100;
        while (newExp >= maxExp) {
            newExp -= maxExp;
            newLevel++;
            maxExp = (newLevel+1)*100;
        }
        var debrisTag = result.getOrCreateTagElement("boundDebris");
        debrisTag.putInt("level", newLevel);
        debrisTag.putInt("exp", newExp);
        debrisTag.putInt("maxExp", maxExp);
        return result;
    }
    
    // 工具方法：创建更换槽位后的物品
    private static ItemStack createSlotChangedItem(ItemStack item, ItemStack switchMaterial) {
        ItemStack result = item.copy();
        CompoundTag tag = result.getTag();

        String slot = "mainhand";
        String newType = null;
        switch (ForgeRegistries.ITEMS.getKey(switchMaterial.getItem()).toString()) {
            case "minecraft:stick":
                slot = "mainhand";
                break;
            case "minecraft:spider_eye":
                slot = "head";
                newType = "helmet";
                break;
            case "minecraft:rotten_flesh":
                slot = "chest";
                newType = "chestplate";
                break;
            case "minecraft:bone":
                slot = "legs";
                newType = "leggings";
                break;
            case "minecraft:string":
                slot = "feet";
                newType = "boots";
                break;
            default:
                return ItemStack.EMPTY;
        }

        if (tag != null && tag.contains("AttributeModifiers")) {
            ListTag modifiersList = tag.getList("AttributeModifiers", 10);
            if (tag.contains("boundDebris")) {
                for (int i = 0; i < modifiersList.size(); i++) {
                    CompoundTag modifierTag = modifiersList.getCompound(i);
                    modifierTag.putString("Slot", slot);
                }
            }
            CompoundTag debrisTag = tag.getCompound("boundDebris");
            String type = debrisTag.getString("type");
            type = newType == null ? type : newType;
            debrisTag.putString("type", type);
            return result;
        }
        
        return ItemStack.EMPTY;
    }

    // 工具方法：从字符串获取物品或标签
    private static Ingredient getIngredientFromString(String itemId) {
        try {
            // 检查是否是标签（以#开头）
            if (itemId.startsWith("#")) {
                // 处理标签
                String tagString = itemId.substring(1); // 去掉#号
                ResourceLocation tagLocation = ResourceLocation.parse(tagString);
                
                // 创建标签key
                var tagKey = net.minecraft.tags.TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), tagLocation);
                
                // 返回标签对应的Ingredient
                return Ingredient.of(tagKey);
            } else {
                // 处理普通物品ID
                ResourceLocation resourceLocation = ResourceLocation.parse(itemId);
                Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                
                if (item != null) {
                    return Ingredient.of(item);
                } else {
                    DLG.LOGGER.error("物品未找到: {}", itemId);
                    return Ingredient.of(Items.IRON_INGOT); // 备用物品
                }
            }
        } catch (Exception e) {
            DLG.LOGGER.error("解析物品ID/标签失败: {}, 错误: {}", itemId, e.getMessage());
            return Ingredient.of(Items.IRON_INGOT); // 备用物品
        }
    }
}
