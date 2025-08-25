package com.core.DLG.configs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                json.addProperty("maxStackSize",64);
                json.addProperty("itemCooldowns",false);
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
        return data.get("maxStackSize").getAsInt();
    }

    public static boolean getItemCooldowns(){
        return data.get("itemCooldowns").getAsBoolean();
    }
}
