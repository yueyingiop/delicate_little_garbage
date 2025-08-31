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
}
