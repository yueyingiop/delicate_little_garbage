package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerConfig {
    public static boolean init = false;
    public static JsonObject data = new JsonObject();

    public static void init() throws IOException {
        if(!init){
            Path configPath = Paths.get("config/DLG/player-config.json");
            File file = configPath.toFile();

            Path parentDir = configPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if(!file.exists()){
                JsonObject json = new JsonObject();
                json.addProperty("cancelAttackGap", true); // 是否取消攻击间隔
                json.addProperty("cancelInvulnerableDuration", true); // 是否取消无伤害
                json.addProperty("alwaysEat",true); // 是否总是吃
                json.addProperty("alwaysSleep",true); // 是否总是睡觉
                json.addProperty("sleepDurationTime",12000); // 睡觉时间
                json.addProperty("sleepEverywhere",true); // 可以在任何地方睡觉
                

                JsonObject foodData = new JsonObject();
                json.add("foodData", foodData);
                foodData.addProperty("maxHungry",40);
                foodData.addProperty("minHealFoodLevel",20);

                JsonObject damageHUD = new JsonObject();
                json.add("damageHUD", damageHUD);
                damageHUD.addProperty("show", false);

                Files.write(file.toPath(),json.toString().getBytes());
            } else {
                detectConfig();
            }
            data = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
            init = true;
        }
    }

    public static void reload() throws IOException {
        init = false;
        init();
    }

    public static void detectConfig() throws IOException {
        Path configPath = Paths.get("config/DLG/player-config.json");
        File file = configPath.toFile();
        int isTrue = 0;
        JsonObject currentData = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        if (currentData.get("cancelAttackGap")==null) {
            currentData.addProperty("cancelAttackGap", true);
            isTrue++;
        }
        if (currentData.get("cancelInvulnerableDuration")==null) {
            currentData.addProperty("cancelInvulnerableDuration", true);
            isTrue++;
        }
        if (currentData.get("alwaysEat")==null) {
            currentData.addProperty("alwaysEat",true);
            isTrue++;
        }
        if (currentData.get("alwaysSleep")==null) {
            currentData.addProperty("alwaysSleep",true);
            isTrue++;
        }
        if (currentData.get("sleepDurationTime")==null) {
            currentData.addProperty("sleepDurationTime",12000);
            isTrue++;
        }
        if (currentData.get("sleepEverywhere")==null) {
            currentData.addProperty("sleepEverywhere",true);
            isTrue++;
        }
        if (currentData.get("foodData")==null) {
            JsonObject foodData = new JsonObject();
            currentData.add("foodData", foodData);
            foodData.addProperty("maxHungry",40);
            foodData.addProperty("minHealFoodLevel",20);
            isTrue++;
        }
        if (currentData.get("damageHUD")==null) {
            JsonObject damageHUD = new JsonObject();
            currentData.add("damageHUD", damageHUD);
            damageHUD.addProperty("show", false);
            isTrue++;
        }
        if (isTrue > 0) Files.write(file.toPath(),currentData.toString().getBytes());
        data = currentData;
    }

    public static boolean getCancelAttackGap() {
        return data.get("cancelAttackGap").getAsBoolean();
    }

    public static boolean getCancelInvulnerableDuration() {
        return data.get("cancelInvulnerableDuration").getAsBoolean();
    }

    public static boolean getAlwaysEat() {
        return data.get("alwaysEat").getAsBoolean();
    }

    public static boolean getAlwaysSleep() {
        return data.get("alwaysSleep").getAsBoolean();
    }

    public static int getSleepDurationTime() {
        return Math.max(0, Math.min(data.get("sleepDurationTime").getAsInt(), 24000));
    }

    public static boolean getSleepEverywhere() {
        return data.get("sleepEverywhere").getAsBoolean();
    }

    private static JsonObject getFoodData(){
        return data.get("foodData").getAsJsonObject();
    }

    public static int getMaxHungry() {
        return Math.max(1, Math.min(getFoodData().get("maxHungry").getAsInt(), Integer.MAX_VALUE-1));
    }

    public static int getMinHealFoodLevel() {
        return Math.max(0, Math.min(getFoodData().get("minHealFoodLevel").getAsInt(), getMaxHungry()));
    }

    private static JsonObject getDamageHUD(){
        return data.get("damageHUD").getAsJsonObject();
    }

    public static boolean getDamageHUDShow() {
        return getDamageHUD().get("show").getAsBoolean();
    }
}
