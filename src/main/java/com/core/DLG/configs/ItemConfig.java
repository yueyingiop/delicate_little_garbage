package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
                json.addProperty("maxStackSize",65525);
                json.addProperty("itemCooldowns",true);

                // 装备碎片
                JsonObject equipmentDebris = new JsonObject();
                JsonArray configList = new JsonArray();
                JsonArray alwaysList = new JsonArray();
                json.add("equipmentDebris",equipmentDebris);
                equipmentDebris.addProperty("itemBrokenDrops",true);
                equipmentDebris.addProperty("alwaysDrops", true);
                equipmentDebris.add("configList",configList);
                configListInit(configList);
                equipmentDebris.add("alwaysList", alwaysList);
                alwaysList.add("forge:armors");
                alwaysList.add("forge:tools");

                // 自定义双爆
                JsonObject C2C = new JsonObject();
                JsonArray C2CItemList = new JsonArray();
                json.add("C&C", C2C);
                C2C.addProperty("customC&C", true);
                C2C.addProperty("playerDefaultCriticalChance", 0.05D);
                C2C.addProperty("playerDefaultCriticalDamage", 0.5D);
                C2C.add("C&CItemList", C2CItemList);
                C2CItemListInit(C2CItemList);
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

    public static void detectConfig() throws IOException {
        Path configPath = Paths.get("config/DLG/item-config.json");
        File file = configPath.toFile();
        JsonObject currentData = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        int isTrue = 0;
        if (currentData.get("maxStackSize") == null) {
            currentData.addProperty("maxStackSize",65525);
            isTrue++;
        }
        if (currentData.get("itemCooldowns") == null) {
            currentData.addProperty("itemCooldowns",true);
            isTrue++;
        }
        if (currentData.get("equipmentDebris") == null) {
            JsonObject equipmentDebris = new JsonObject();
            JsonArray configList = new JsonArray();
            JsonArray alwaysList = new JsonArray();
            currentData.add("equipmentDebris",equipmentDebris);
            equipmentDebris.addProperty("itemBrokenDrops",true);
            equipmentDebris.addProperty("alwaysDrops", true);
            equipmentDebris.add("configList",configList);
            configListInit(configList);
            equipmentDebris.add("alwaysList", alwaysList);
            alwaysList.add("#forge:armors");
            alwaysList.add("#forge:tools");
            isTrue++;
        }
        if (currentData.get("C&C") == null) {
            JsonObject C2C = new JsonObject();
            JsonArray C2CItemList = new JsonArray();
            currentData.add("C&C", C2C);
            C2C.addProperty("customC&C", true);
            C2C.addProperty("playerDefaultCriticalChance", 0.05D);
            C2C.addProperty("playerDefaultCriticalDamage", 0.5D);
            C2C.add("C&CItemList", C2CItemList);
            C2CItemListInit(C2CItemList);
            isTrue++;
        }
        if (isTrue > 0) Files.write(file.toPath(),currentData.toString().getBytes());
        data = currentData;
    }

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
    //#endregion
    
    public static int getMaxStackSize(){
        return Math.max(1, Math.min(data.get("maxStackSize").getAsInt(), Integer.MAX_VALUE-1));
    }

    public static boolean getItemCooldowns(){
        return data.get("itemCooldowns").getAsBoolean();
    }

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

    private static JsonObject getC2C(){
        return data.get("C&C").getAsJsonObject();
    }

    public static boolean getCustomC2C() {
        return getC2C().get("customC&C").getAsBoolean();
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

    // 格式化文字
    private static String formatItemName(String itemName) {
        return itemName.startsWith("#") ? itemName.substring(1) : itemName;
    }
}
