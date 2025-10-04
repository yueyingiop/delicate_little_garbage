package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.item.RegistryItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemConfig {
    public static boolean init = false;
    public static JsonObject data = new JsonObject();
    public static void init() throws IOException{
        if(!init){
            Path configPath = Paths.get("config/DLG/item-config.json");
            File file = configPath.toFile();

            Path parentDir = configPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if(!file.exists()){
                JsonObject json = new JsonObject();
                json.addProperty("maxStackSize",64);
                json.addProperty("cancelItemCooldowns",true);

                // 装备碎片
                JsonObject equipmentDebris = new JsonObject();
                JsonArray configList = new JsonArray();
                JsonArray alwaysList = new JsonArray();
                JsonArray upgradeMaterials = new JsonArray();
                JsonArray expMaterials = new JsonArray();
                json.add("equipmentDebris",equipmentDebris);
                equipmentDebris.addProperty("itemBrokenDrops",true);
                equipmentDebris.addProperty("alwaysDrops", true);
                equipmentDebris.add("configList",configList);
                configListInit(configList);
                equipmentDebris.add("alwaysList", alwaysList);
                alwaysList.add("forge:armors");
                alwaysList.add("forge:tools");
                equipmentDebris.add("upgradeMaterials",upgradeMaterials);
                equipmentDebris.add("expMaterials",expMaterials);
                upgradeMaterialsInit(upgradeMaterials);
                expMaterialsInit(expMaterials);

                // 自定义双爆
                JsonObject C2C = new JsonObject();
                JsonArray C2CItemList = new JsonArray();
                json.add("C&C", C2C);
                C2C.addProperty("customAttribute", true);
                C2C.addProperty("playerDefaultCriticalChance", 0.05D);
                C2C.addProperty("playerDefaultCriticalDamage", 0.5D);
                C2C.add("C&CItemList", C2CItemList);
                C2CItemListInit(C2CItemList);

                JsonObject customAttribute = new JsonObject();
                JsonObject attributeType = new JsonObject();
                JsonArray entrySetting = new JsonArray();
                json.add("customAttribute", customAttribute);
                customAttribute.add("attributeType", attributeType);
                customAttribute.add("entrySetting", entrySetting);
                attributeTypeInit(attributeType);
                entrySettingInit(entrySetting);

                Files.write(file.toPath(),json.toString().getBytes());
            } else {
                detectConfig();
            }
            data = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
            init = true;
        }
    }

    //#region 配置加载函数
    public static void reload() throws IOException {
        init = false;
        init();
    }

    // 自动检测配置文件缺失项并补全
    public static void detectConfig() throws IOException {
        Path configPath = Paths.get("config/DLG/item-config.json");
        File file = configPath.toFile();
        JsonObject currentData = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        int isTrue = 0;
        if (currentData.get("maxStackSize") == null) {
            currentData.addProperty("maxStackSize",64);
            isTrue++;
        }
        if (currentData.get("cancelItemCooldowns") == null) {
            currentData.addProperty("cancelItemCooldowns",true);
            isTrue++;
        }
        if (currentData.get("equipmentDebris") == null) {
            JsonObject equipmentDebris = new JsonObject();
            JsonArray configList = new JsonArray();
            JsonArray alwaysList = new JsonArray();
            JsonArray upgradeMaterials = new JsonArray();
            JsonArray expMaterials = new JsonArray();
            currentData.add("equipmentDebris",equipmentDebris);
            equipmentDebris.addProperty("itemBrokenDrops",true);
            equipmentDebris.addProperty("alwaysDrops", true);
            equipmentDebris.add("configList",configList);
            configListInit(configList);
            equipmentDebris.add("alwaysList", alwaysList);
            alwaysList.add("#forge:armors");
            alwaysList.add("#forge:tools");
            equipmentDebris.add("upgradeMaterials",upgradeMaterials);
            equipmentDebris.add("expMaterials",expMaterials);
            upgradeMaterialsInit(upgradeMaterials);
            expMaterialsInit(expMaterials);
            isTrue++;
        }
        if (currentData.get("C&C") == null) {
            JsonObject C2C = new JsonObject();
            JsonArray C2CItemList = new JsonArray();
            currentData.add("C&C", C2C);
            C2C.addProperty("customAttribute", true);
            C2C.addProperty("playerDefaultCriticalChance", 0.05D);
            C2C.addProperty("playerDefaultCriticalDamage", 0.5D);
            C2C.add("C&CItemList", C2CItemList);
            C2CItemListInit(C2CItemList);
            isTrue++;
        }
        if (currentData.get("customAttribute") == null) {
            JsonObject customAttribute = new JsonObject();
            JsonObject attributeType = new JsonObject();
            JsonArray entrySetting = new JsonArray();
            currentData.add("customAttribute", customAttribute);
            customAttribute.add("attributeType", attributeType);
            customAttribute.add("entrySetting", entrySetting);
            attributeTypeInit(attributeType);
            entrySettingInit(entrySetting);
            isTrue++;
        }
        if (isTrue > 0) Files.write(file.toPath(),currentData.toString().getBytes());
        data = currentData;
    }

    // 装备碎片配置列表初始化
    private static void configListInit(JsonArray configList) {
        String[] specialItems = {
            "minecraft:trident",
            "minecraft:bow",
            "minecraft:crossbow",
            "minecraft:turtle_helmet"
        };
        String[] toolItems = { 
            "minecraft:wooden_shovel",
            "minecraft:wooden_pickaxe",
            "minecraft:wooden_axe",
            "minecraft:wooden_hoe",
            "minecraft:wooden_sword",
            "minecraft:stone_shovel",
            "minecraft:stone_pickaxe",
            "minecraft:stone_axe",
            "minecraft:stone_hoe",
            "minecraft:stone_sword",
            "minecraft:iron_shovel",
            "minecraft:iron_pickaxe",
            "minecraft:iron_axe",
            "minecraft:iron_hoe",
            "minecraft:iron_sword",
            "minecraft:golden_shovel",
            "minecraft:golden_pickaxe",
            "minecraft:golden_axe",
            "minecraft:golden_hoe",
            "minecraft:golden_sword",
            "minecraft:diamond_shovel",
            "minecraft:diamond_pickaxe",
            "minecraft:diamond_axe",
            "minecraft:diamond_hoe",
            "minecraft:diamond_sword",
            "minecraft:netherite_shovel",
            "minecraft:netherite_pickaxe",
            "minecraft:netherite_axe",
            "minecraft:netherite_hoe",
            "minecraft:netherite_sword"
        };
        String[] armorItems = { 
            "minecraft:leather_helmet",
            "minecraft:leather_chestplate",
            "minecraft:leather_leggings",
            "minecraft:leather_boots",
            "minecraft:chainmail_helmet",
            "minecraft:chainmail_chestplate",
            "minecraft:chainmail_leggings",
            "minecraft:chainmail_boots",
            "minecraft:iron_helmet",
            "minecraft:iron_chestplate",
            "minecraft:iron_leggings",
            "minecraft:iron_boots",
            "minecraft:golden_helmet",
            "minecraft:golden_chestplate",
            "minecraft:golden_leggings",
            "minecraft:golden_boots",
            "minecraft:diamond_helmet",
            "minecraft:diamond_chestplate",
            "minecraft:diamond_leggings",
            "minecraft:diamond_boots",
            "minecraft:netherite_helmet",
            "minecraft:netherite_chestplate",
            "minecraft:netherite_leggings",
            "minecraft:netherite_boots"
        };
        TypeEnum[] specialTypes = {
            TypeEnum.TRIDENT,
            TypeEnum.BOW,
            TypeEnum.CROSSBOW,
            TypeEnum.HELMET
        };
        QualityEnum[] specialQualities = {
            QualityEnum.DEFAULT_QUALITY,
            QualityEnum.DEFAULT_QUALITY,
            QualityEnum.DEFAULT_QUALITY,
            QualityEnum.QUALITY3
        };

        // 创建特殊物品配置项
        for (int i = 0; i < specialItems.length; i++) {
            JsonObject itemConfig = new JsonObject();
            itemConfig.addProperty("item", specialItems[i]);
            itemConfig.addProperty("type", specialTypes[i].getString());
            itemConfig.addProperty("quality", specialQualities[i].getNumber());
            configList.add(itemConfig);
        }

        // 创建工具物品配置项
        for (int i = 0; i < toolItems.length; i++) {
            JsonObject itemConfig = new JsonObject();
            String type = toolItems[i].split(":")[1].split("_")[1];
            int quality;
            switch (toolItems[i].split(":")[1].split("_")[0]) {
                case "wooden":
                    quality = 1;
                    break;
                case "stone":
                    quality = 2;
                    break;
                case "iron":
                    quality = 3;
                    break;
                case "golden":
                    quality = 4;
                    break;
                case "diamond":
                    quality = 5;
                    break;
                case "netherite":
                    quality = 6;
                    break;
                default:
                    quality = -1;
                    break;
            }
            itemConfig.addProperty("item", toolItems[i]);
            itemConfig.addProperty("type", type);
            itemConfig.addProperty("quality", quality);
            configList.add(itemConfig);
        }
    
        // 创建护甲物品配置项
        for (int i = 0; i < armorItems.length; i++) { 
            JsonObject itemConfig = new JsonObject();
            String type = armorItems[i].split(":")[1].split("_")[1];
            int quality;
            switch (armorItems[i].split(":")[1].split("_")[0]) { 
                case "leather":
                    quality = 1;
                    break;
                case "chainmail":
                    quality = 2;
                    break;
                case "iron":
                    quality = 3;
                    break;
                case "golden":
                    quality = 4;
                    break;
                case "diamond":
                    quality = 5;
                    break;
                case "netherite":
                    quality = 6;
                    break;
                default:
                    quality = -1;
                    break;
            }
            itemConfig.addProperty("item", armorItems[i]);
            itemConfig.addProperty("type", type);
            itemConfig.addProperty("quality", quality);
            configList.add(itemConfig);
        }
    }

    // C&C物品配置列表初始化
    private static void C2CItemListInit(JsonArray configList){ 
        String[] C2CItems = {
            "#minecraft:swords",
            "#minecraft:axes",
        };
        Double[] itemCriticalChance = {
            0.15D,
            0.25D
        };
        Double[] itemCriticalDamage = {
            0.5D,
            0.7D
        };
        for (int i = 0; i < C2CItems.length; i++) { 
            JsonObject itemConfig = new JsonObject();
            itemConfig.addProperty("item", C2CItems[i]);
            itemConfig.addProperty("criticalChance", itemCriticalChance[i]);
            itemConfig.addProperty("criticalDamage", itemCriticalDamage[i]);
            configList.add(itemConfig);
        }
    }

    // 升级材料初始化
    public static void upgradeMaterialsInit(JsonArray configList){ 
        String[] upgradeMaterials = {
            "#minecraft:stone_tool_materials",
            "minecraft:iron_ingot",
            "minecraft:gold_ingot",
            "minecraft:diamond",
            "minecraft:netherite_ingot",
            "minecraft:nether_star",
            "delicate_little_garbage:indestructible_scroll"
        };
        for (int i = 0; i < upgradeMaterials.length; i++) { 
            JsonObject itemConfig = new JsonObject();
            itemConfig.addProperty("quality", i+1);
            itemConfig.addProperty("upgradeMaterial", upgradeMaterials[i]);
            configList.add(itemConfig);
        }
    }
    // 经验材料初始化
    public static void expMaterialsInit(JsonArray configList){
        String[] MaterialList = {
            "delicate_little_garbage:life_crystal",
            "delicate_little_garbage:delicate_little_garbage"
        };
        int[] expList = {
            100,
            2000
        };
        for (int i = 0; i < MaterialList.length; i++) { 
            JsonObject itemConfig = new JsonObject();
            itemConfig.addProperty("expMaterial", MaterialList[i]);
            itemConfig.addProperty("exp", expList[i]);
            configList.add(itemConfig);
        }
    }
    
    public static void attributeTypeInit(JsonObject attributeType) {
        JsonArray customType = new JsonArray();
        JsonArray attackType = new JsonArray();
        JsonArray defenseType = new JsonArray();
        attributeType.add("customType", customType);
        attributeType.add("attackType", attackType);
        attributeType.add("defenseType", defenseType);
        customTypeInit(customType);
        attackTypeInit(attackType);
        defenseTypeInit(defenseType);
    }

    public static void customTypeInit(JsonArray customType) {
        String[] attributeList = {
            "delicate_little_garbage:critical_chance", //暴击率
            "delicate_little_garbage:critical_damage", //暴击伤害
            "minecraft:generic.flying_speed", //飞行速度
            "minecraft:generic.follow_range", //生物跟随范围
            "minecraft:generic.max_health", //最大生命值
            "minecraft:generic.max_health", //最大生命值
            "forge:block_reach", // 方块距离
            "forge:entity_reach" // 实体距离
        };
        float[] baseValueList = {
            0.06f,
            0.13f,
            1.0f,
            5.0f,
            10.0f,
            0.15f,
            0.3f,
            0.3f
        };
        float[] baseRangeList = {
            0.015f,
            0.03f,
            0.3f,
            1.0f,
            3.0f,
            0.03f,
            0.1f,
            0.1f
        };
        float[] updateValueList = {
            0.05f,
            0.11f,
            0.5f,
            3.0f,
            3.0f,
            0.1f,
            0.15f,
            0.15f
        };
        float[] updateRangeList = {
            0.003f,
            0.006f,
            0.1f,
            0.5f,
            1.0f,
            0.05f,
            0.05f,
            0.05f
        };
        int[] operationList = {
            0,
            0,
            2,
            0,
            0,
            2,
            0,
            0
        };
        for (int i = 0; i < attributeList.length; i++) { 
            JsonObject attributeConfig = new JsonObject();
            attributeConfig.addProperty("attribute", attributeList[i]);
            attributeConfig.addProperty("baseValue", baseValueList[i]);
            attributeConfig.addProperty("baseRange", baseRangeList[i]);
            attributeConfig.addProperty("updateValue", updateValueList[i]);
            attributeConfig.addProperty("updateRange", updateRangeList[i]);
            attributeConfig.addProperty("operation", operationList[i]);
            customType.add(attributeConfig);
        }
    }

    public static void attackTypeInit(JsonArray customType) {
        String[] attributeList = {
            "delicate_little_garbage:critical_chance", //暴击率
            "delicate_little_garbage:critical_damage", //暴击伤害
            "delicate_little_garbage:lifesteal_chance", //吸血率
            "delicate_little_garbage:lifesteal_damage", //吸血伤害
            "delicate_little_garbage:penetration_chance", //穿透率
            "delicate_little_garbage:penetration_damage", //穿透伤害
            "minecraft:generic.attack_damage", //攻击伤害
            "minecraft:generic.attack_damage", //攻击伤害%
            "minecraft:generic.attack_knockback", //攻击击退
            "minecraft:generic.attack_speed", //攻击速度
            "minecraft:generic.max_health", //最大生命值
            "minecraft:generic.max_health", //最大生命值%
        };
        float[] baseValueList = {
            0.08f,
            0.17f,
            0.1f,
            0.2f,
            0.1f,
            0.15f,
            5.0f,
            0.2f,
            0.3f,
            0.3f,
            3.0f,
            0.15f
        };
        float[] baseRangeList = {
            0.015f,
            0.03f,
            0.02f,
            0.07f,
            0.02f,
            0.04f,
            1.0f,
            0.02f,
            0.05f,
            0.05f,
            0.5f,
            0.03f
        };
        float[] updateValueList = {
            0.07f,
            0.12f,
            0.08f,
            0.15f,
            0.08f,
            0.1f,
            4.0f,
            0.12f,
            0.25f,
            0.25f,
            2.5f,
            0.1f
        };
        float[] updateRangeList = {
            0.005f,
            0.004f,
            0.01f,
            0.03f,
            0.02f,
            0.03f,
            0.5f,
            0.045f,
            0.03f,
            0.03f,
            0.5f,
            0.02f
        };
        int[] operationList = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            2,
            2,
            2,
            0,
            2
        };
        for (int i = 0; i < attributeList.length; i++) { 
            JsonObject attributeConfig = new JsonObject();
            attributeConfig.addProperty("attribute", attributeList[i]);
            attributeConfig.addProperty("baseValue", baseValueList[i]);
            attributeConfig.addProperty("baseRange", baseRangeList[i]);
            attributeConfig.addProperty("updateValue", updateValueList[i]);
            attributeConfig.addProperty("updateRange", updateRangeList[i]);
            attributeConfig.addProperty("operation", operationList[i]);
            customType.add(attributeConfig);
        }
    }

    public static void defenseTypeInit(JsonArray customType) {
        String[] attributeList = {
            "delicate_little_garbage:critical_chance", //暴击率
            "delicate_little_garbage:critical_damage", //暴击伤害
            "delicate_little_garbage:dodge", //闪避
            "delicate_little_garbage:healing_bonus", //治疗加成
            "minecraft:generic.armor", //护甲
            "minecraft:generic.armor_toughness", //护甲韧性
            "minecraft:generic.knockback_resistance", //击退抗性
            "minecraft:generic.luck", //幸运值
            "minecraft:generic.movement_speed", //移动速度
            "forge:swim_speed", //游泳速度
            "minecraft:generic.max_health", //最大生命值
            "minecraft:generic.max_health", //最大生命值
        };
        float[] baseValueList = {
            0.06f,
            0.13f,
            0.15f,
            3.0f,
            5.0f,
            2.0f,
            0.1f,
            1.0f,
            0.25f,
            0.25f,
            10.0f,
            0.15f
        };
        float[] baseRangeList = {
            0.015f,
            0.03f,
            0.05f,
            1.0f,
            1.0f,
            0.5f,
            0.03f,
            0.4f,
            0.1f,
            0.1f,
            3.0f,
            0.03f
        };
        float[] updateValueList = {
            0.05f,
            0.11f,
            0.12f,
            1.0f,
            1.0f,
            0.5f,
            0.05f,
            0.5f,
            0.1f,
            0.1f,
            3.0f,
            0.1f
        };
        float[] updateRangeList = {
            0.003f,
            0.006f,
            0.02f,
            0.3f,
            0.5f,
            0.15f,
            0.01f,
            0.3f,
            0.03f,
            0.03f,
            1.0f,
            0.05f
        };
        int[] operationList = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            2,
            2,
            0,
            2
        };
        for (int i = 0; i < attributeList.length; i++) { 
            JsonObject attributeConfig = new JsonObject();
            attributeConfig.addProperty("attribute", attributeList[i]);
            attributeConfig.addProperty("baseValue", baseValueList[i]);
            attributeConfig.addProperty("baseRange", baseRangeList[i]);
            attributeConfig.addProperty("updateValue", updateValueList[i]);
            attributeConfig.addProperty("updateRange", updateRangeList[i]);
            attributeConfig.addProperty("operation", operationList[i]);
            customType.add(attributeConfig);
        }
    }

    public static void entrySettingInit(JsonArray entrySetting) {
        int[] qualityList = {
            1,2,3,4,5,6,7,8,9,0
        };
        int[] basicEntriesCount = {
            1,2,3,3,4,4,5,5,6,4
        };
        int[] maxEntriesCount = {
            1,2,3,4,5,5,6,6,7,5
        };
        for (int i = 0; i < qualityList.length; i++) { 
            JsonObject entrySettingConfig = new JsonObject();
            entrySettingConfig.addProperty("quality", qualityList[i]);
            entrySettingConfig.addProperty("basicEntriesCount", basicEntriesCount[i]);
            entrySettingConfig.addProperty("maxEntriesCount", maxEntriesCount[i]);
            entrySetting.add(entrySettingConfig);
        }
    }
    //#endregion
    
    public static int getMaxStackSize(){
        return Math.max(1, Math.min(data.get("maxStackSize").getAsInt(), Integer.MAX_VALUE-1));
    }

    public static boolean getCancelItemCooldowns(){
        return data.get("cancelItemCooldowns").getAsBoolean();
    }

    //#region 装备碎片配置相关函数
    private static JsonObject getEquipmentDebris(){ 
        return data.get("equipmentDebris").getAsJsonObject();
    }

    public static boolean getItemBrokenDrops(){ 
        return getEquipmentDebris().get("itemBrokenDrops").getAsBoolean();
    }

    public static boolean getAlwaysDrops() {
        return getEquipmentDebris().get("alwaysDrops").getAsBoolean();
    }

    public static JsonArray getConfigList(){
        return getEquipmentDebris().get("configList").getAsJsonArray();
    }

    public static boolean itemInConfigList(String itemName){ 
        JsonArray configList = getConfigList();
        for(int i = 0; i < configList.size(); i++){ 
            JsonObject itemConfig = configList.get(i).getAsJsonObject();
            if (
                itemConfig.has("item") && 
                formatItemName(
                    itemConfig.get("item").getAsString()
                ).equals(itemName)
            ) {
                return true;
            }
        }
        return false;
    }

    public static JsonObject getConfig(String itemName){ 
        JsonArray configList = getConfigList();
        for(int i = 0; i < configList.size(); i++) {
            JsonObject itemConfig = configList.get(i).getAsJsonObject();
            if (
                itemConfig.has("item") && 
                formatItemName(
                    itemConfig.get("item").getAsString()
                ).equals(itemName)
            ) {
                return itemConfig;
            }
        }
        return null;
    }

    public static JsonArray getAlwaysList(){
        return getEquipmentDebris().get("alwaysList").getAsJsonArray();
    }

    public static boolean itemInAlwaysList(String itemName) {
        JsonArray alwaysList = getAlwaysList();
        for(int i = 0; i < alwaysList.size(); i++) {
            if (
                formatItemName(
                    alwaysList.get(i).getAsString()
                ).equals(itemName)
            ) {
                return true;
            }
        }
        return false;
    }

    // 升级材料列表
    public static JsonArray getupgradeMaterialsList(){
        return getEquipmentDebris().get("upgradeMaterials").getAsJsonArray();
    }

    // 升级材料是否在列表内
    public static boolean itemInUpgradeMaterialsList(String itemName) {
        JsonArray upgradeMaterialsList = getupgradeMaterialsList();
        for(int i = 0; i < upgradeMaterialsList.size(); i++) { 
            JsonObject itemConfig = upgradeMaterialsList.get(i).getAsJsonObject();
            if (
                itemConfig.has("upgradeMaterial") &&
                formatItemName(
                    itemConfig.get("upgradeMaterial").getAsString()
                ).equals(itemName)
            ) {
                return true;
            }
        }
        return false;
    }
    
    // 升级检测
    public static boolean upgradeDetect(ItemStack itemStack, ItemStack materialStack) {
        if (itemStack.isEmpty() || materialStack.isEmpty()) return false;
        if (itemStack.getItem() != RegistryItem.EQUIPMENT_DEBRIS.get()) return false;
        int itemQuality = itemStack.getOrCreateTag().getInt("quality");
        if (itemQuality <= 0) return false;
        JsonArray itemConfig = getupgradeMaterialsList();

        String materialId = ForgeRegistries.ITEMS.getKey(materialStack.getItem()).toString();
        Boolean isTrue = false;
        for (var tagkey : materialStack.getTags().toList()) {
            isTrue = itemInUpgradeMaterialsList(materialId) || itemInUpgradeMaterialsList(tagkey.location().toString());
            if (isTrue) break;
        }
        // 获取升级材料
        for(int i = 0; i < itemConfig.size(); i++) { 
            JsonObject itemMaterial = itemConfig.get(i).getAsJsonObject();
            if (
                itemMaterial.get("quality").getAsInt() == itemQuality && isTrue
            ) {
                return true;
            }
        }
        return false;
    }
    
    // 经验材料列表
    public static JsonArray getExpMaterialsList() { 
        return getEquipmentDebris().get("expMaterials").getAsJsonArray();
    }
    
    // 经验材料是否在列表内(String)
    public static boolean itemInExpMaterialsList(String itemName) { 
        JsonArray expMaterialsList = getExpMaterialsList();
        for(int i = 0; i < expMaterialsList.size(); i++) { 
            JsonObject itemConfig = expMaterialsList.get(i).getAsJsonObject();
            if (
                itemConfig.has("expMaterial") &&
                formatItemName(
                    itemConfig.get("expMaterial").getAsString()
                ).equals(itemName)
            ) {
                return true;
            }
        }
        return false;
    }

    // 获取经验值(ItemStack)
    public static int getExp(ItemStack itemStack) { 
        String itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
        Boolean isTrue = false;
        String itemName = "";
        if (itemStack.getTags().toList().size() == 0) return getExp(itemId);
        for (var tagkey : itemStack.getTags().toList()) {
            isTrue = itemInExpMaterialsList(itemId) || itemInExpMaterialsList(tagkey.location().toString());
            if (isTrue) {
                itemName = itemInExpMaterialsList(itemId) ? itemId : tagkey.location().toString();
                return getExp(itemName);
            };
        }
        
        return -1;
    }

    // 获取经验值(String)
    public static int getExp(String itemName) { 
        JsonArray expMaterialsList = getExpMaterialsList();
        for(int i = 0; i < expMaterialsList.size(); i++) { 
            JsonObject itemConfig = expMaterialsList.get(i).getAsJsonObject();
            if (itemInExpMaterialsList(itemName) && itemName.equals(itemConfig.get("expMaterial").getAsString())) {
                return Math.max(0, itemConfig.get("exp").getAsInt());
            }
        }
        
        return -1;
    }
    //#endregion



    //#region C&C配置相关函数
    private static JsonObject getC2C(){
        return data.get("C&C").getAsJsonObject();
    }

    public static boolean getCustomAttribute() {
        return getC2C().get("customAttribute").getAsBoolean();
    }

    public static Double getPlayerDefaultCriticalChance() {
        return getC2C().get("playerDefaultCriticalChance").getAsDouble();
    }

    public static Double getPlayerDefaultCriticalDamage() {
        return getC2C().get("playerDefaultCriticalDamage").getAsDouble();
    }

    public static JsonArray getC2CItemList(){
        return getC2C().get("C&CItemList").getAsJsonArray();
    }

    public static boolean itemInC2CItemList(String itemName) { 
        JsonArray C2CItemList = getC2CItemList();
        for(int i = 0; i < C2CItemList.size(); i++) { 
            JsonObject itemConfig = C2CItemList.get(i).getAsJsonObject();
            if (
                itemConfig.has("item") &&
                formatItemName(
                    itemConfig.get("item").getAsString()
                ).equals(itemName)
            ) {
                return true;
            }
        }
        return false;
    }

    public static JsonObject getC2CItemConfig(String itemName) { 
        JsonArray C2CItemList = getC2CItemList();
        for(int i = 0; i < C2CItemList.size(); i++) { 
                JsonObject itemConfig = C2CItemList.get(i).getAsJsonObject();
                if (
                    itemConfig.has("item") &&
                    formatItemName(
                        itemConfig.get("item").getAsString()
                    ).equals(itemName)
                ) {
                    return itemConfig;
                }
            }
        return null;
    }
    //#endregion


    //#region 词条相关函数
    private static JsonObject getCustomAttributeO(){
        return data.get("customAttribute").getAsJsonObject();
    }

    private static JsonObject getAttributeType() {
        return getCustomAttributeO().get("attributeType").getAsJsonObject();
    }

    public static JsonArray getCustomType() {
        return getAttributeType().get("customType").getAsJsonArray();
    }

    public static JsonArray getAttackType() {
        return getAttributeType().get("attackType").getAsJsonArray();
    }

    public static JsonArray getDefenseType() {
        return getAttributeType().get("defenseType").getAsJsonArray();
    }

    private static JsonArray getEntrySetting() {
        return getCustomAttributeO().get("entrySetting").getAsJsonArray();
    }

    public static JsonObject getConfigInEntrySetting(int quality) {
        JsonArray entrySetting = getEntrySetting();
        for(int i = 0; i < entrySetting.size(); i++) { 
            JsonObject itemConfig = entrySetting.get(i).getAsJsonObject();
            if (itemConfig.get("quality").getAsInt() == quality) {
                return itemConfig;
            }
        }
        return null;
    }
    //#endregion

    // 格式化文字
    private static String formatItemName(String itemName) {
        return itemName.startsWith("#") ? itemName.substring(1) : itemName;
    }
}
