package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FoodConfig {
    public static boolean init = false;
    public static JsonObject data = new JsonObject();

    public static void init() throws IOException {
        if(!init){
            Path configPath = Paths.get("config/DLG/food-config.json");
            File file = configPath.toFile();

            Path parentDir = configPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if(!file.exists()){
                JsonObject json = new JsonObject();
                json.addProperty("alwaysEat",true);

                JsonObject foodData = new JsonObject();
                json.add("foodData", foodData);
                foodData.addProperty("maxHungry",20);
                foodData.addProperty("minHealFoodLevel",18);
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

    public static boolean getAlwaysEat() {
        return data.get("alwaysEat").getAsBoolean();
    }

    private static JsonObject getFoodData(){
        return data.get("foodData").getAsJsonObject();
    }

    public static int getMaxHungry() {
        return getFoodData().get("maxHungry").getAsInt();
    }

    public static int getMinHealFoodLevel() {
        return getFoodData().get("minHealFoodLevel").getAsInt();
    }
}
