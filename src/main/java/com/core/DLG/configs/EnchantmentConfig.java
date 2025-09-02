package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EnchantmentConfig {
    public static boolean init = false;
    public static JsonObject data = new JsonObject();

    public static void init() throws IOException {
        if(!init){
            Path configPath = Paths.get("config/DLG/enchantment-config.json");
            File file = configPath.toFile();

            Path parentDir = configPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            if(!file.exists()){
                JsonObject json = new JsonObject();
                json.addProperty("trulyInfinite", true);
                json.addProperty("infinityCrossbow", true);

                JsonObject cancelEnchantmentCombat = new JsonObject();
                json.add("cancelEnchantmentCombat", cancelEnchantmentCombat);
                cancelEnchantmentCombat.addProperty("attack", true);
                cancelEnchantmentCombat.addProperty("protection", true);
                cancelEnchantmentCombat.addProperty("infiniteAndMending", true);
                cancelEnchantmentCombat.addProperty("pierceAndMutiShooting", true);

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
        Path configPath = Paths.get("config/DLG/enchantment-config.json");
        File file = configPath.toFile();
        int isTrue = 0;
        JsonObject currentData = JsonParser.parseString(Files.readString(file.toPath())).getAsJsonObject();
        if (currentData.get("trulyInfinite")==null) {
            currentData.addProperty("trulyInfinite", true);
            isTrue++;
        }
        if (currentData.get("infinityCrossbow")==null) {
            currentData.addProperty("infinityCrossbow", true);
            isTrue++;
        }
        if (currentData.get("cancelEnchantmentCombat")==null) {
            JsonObject cancelEnchantmentCombat = new JsonObject();
            currentData.add("cancelEnchantmentCombat", cancelEnchantmentCombat);
            cancelEnchantmentCombat.addProperty("attack", true);
            cancelEnchantmentCombat.addProperty("protection", true);
            cancelEnchantmentCombat.addProperty("infiniteAndMending", true);
            cancelEnchantmentCombat.addProperty("pierceAndMutiShooting", true);
            isTrue++;
        }
        if (isTrue > 0) Files.write(file.toPath(),currentData.toString().getBytes());
        data = currentData;
    }

    public static boolean getTrulyInfinite() {
        return data.get("trulyInfinite").getAsBoolean();
    }

    public static boolean getInfinityCrossbow() {
        return data.get("infinityCrossbow").getAsBoolean();
    }

    private static JsonObject getCancelEnchantmentCombat() {
        return data.getAsJsonObject("cancelEnchantmentCombat");
    }

    public static boolean getAttack() {
        return getCancelEnchantmentCombat().get("attack").getAsBoolean();
    }

    public static boolean getProtection() {
        return getCancelEnchantmentCombat().get("protection").getAsBoolean();
    }

    public static boolean getInfiniteAndMending() {
        return getCancelEnchantmentCombat().get("infiniteAndMending").getAsBoolean();
    }

    public static boolean getPierceAndMutiShooting() {
        return getCancelEnchantmentCombat().get("pierceAndMutiShooting").getAsBoolean();
    }
}
