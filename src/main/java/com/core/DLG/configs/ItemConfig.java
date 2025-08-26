package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.core.DLG.DLG;
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
                json.addProperty("itemCooldowns",false);
                
                JsonObject equipmentDebris = new JsonObject();
                JsonArray configList = new JsonArray();
                JsonArray alwaysList = new JsonArray();
                JsonObject exampleConfig = new JsonObject();
                json.add("equipmentDebris",equipmentDebris);
                equipmentDebris.addProperty("itemBrokenDrops",true);
                equipmentDebris.addProperty("alwaysDrops", true);
                equipmentDebris.add("configList",configList);
                configList.add(exampleConfig);
                exampleConfig.addProperty("item", "minecraft:wooden_shovel");
                exampleConfig.addProperty("type", TypeEnum.SHOVEL.getString());
                exampleConfig.addProperty("quality", 1);
                exampleConfig.add("alwaysList", alwaysList);
                alwaysList.add("forge:armors");
                alwaysList.add("forge:tools");
                Files.write(file.toPath(),json.toString().getBytes());
            }
            data = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
            init = true;
        }
    }

    public static void reload() throws IOException {
        init = false;
        init();
    }

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

    // 格式化文字
    private static String formatItemName(String itemName) {
        DLG.LOGGER.info("[DEBUG] itemName:"+ (itemName.startsWith("#") ? itemName.substring(1) : itemName));
        return itemName.startsWith("#") ? itemName.substring(1) : itemName;
    }
}
