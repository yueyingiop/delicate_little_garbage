package com.core.DLG.util;

import java.io.IOException;

import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.item.DebrisItem;
import com.core.DLG.item.RegistryItem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemBreakHelper {
    public static void handleItemBreak(Player player, ItemStack itemStack) {
        try { 
            ItemConfig.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
        for (var tagkey : itemStack.getTags().toList()) {
            Boolean isTrue = ItemConfig.itemInConfigList(itemId) || ItemConfig.itemInConfigList(tagkey.location().toString());
            if (isTrue) {
                String name = ItemConfig.itemInConfigList(itemId) ? itemId : tagkey.location().toString();
                TypeEnum type = TypeEnum.getType(ItemConfig.getConfig(name).get("type").getAsString());
                QualityEnum quality = QualityEnum.getQuality(ItemConfig.getConfig(name).get("quality").getAsInt());
                ItemStack debrisItem = ((DebrisItem) RegistryItem.EQUIPMENT_DEBRIS.get()).setDebrisTag(type, quality);
                if (!player.getInventory().add(debrisItem)) player.drop(debrisItem, false);
                // if (!event.getEntity().getInventory().add( ((DebrisItem) RegistryItem.DEBRIS.get()).setDebrisTag(TypeEnum.getType(ItemConfig.getConfig((itemId != null && !itemId.isEmpty()) ? itemId : tagkey.location().toString()).get("type").getAsString())))) event.getEntity().drop( ((DebrisItem) RegistryItem.DEBRIS.get()).setDebrisTag(TypeEnum.getType(ItemConfig.getConfig((itemId != null && !itemId.isEmpty()) ? itemId : tagkey.location().toString()).get("type").getAsString())), false);
                return;
            }
        }

        for (var tagkey : itemStack.getTags().toList()) {
            Boolean isTrue  = ItemConfig.itemInAlwaysList(itemId) || ItemConfig.itemInAlwaysList(tagkey.location().toString());
            if (isTrue && ItemConfig.getAlwaysDrops()) {
                TypeEnum type = TypeEnum.UNDEFINED_TYPE;
                QualityEnum quality = QualityEnum.UNDEFINED_QUALITY;
                ItemStack debrisItem = ((DebrisItem) RegistryItem.EQUIPMENT_DEBRIS.get()).setDebrisTag(type, quality);
                if (!player.getInventory().add(debrisItem)) player.drop(debrisItem, false);
                break;
            }
        }
    }
}
