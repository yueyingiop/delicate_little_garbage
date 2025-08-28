package com.core.DLG.events;

import com.core.DLG.DLG;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.item.DebrisItem;
import com.core.DLG.item.RegistryItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrindstoneEventHandler {
    @SubscribeEvent
    public static void clearDebrisType(GrindstoneEvent.OnPlaceItem event) {
        ItemStack topItem = event.getTopItem();
        ItemStack bottomItem = event.getBottomItem();
        if (topItem.getItem().equals(RegistryItem.EQUIPMENT_DEBRIS.get()) || bottomItem.getItem() == null) {
            // 检测品质存在
            CompoundTag nbt = topItem.getTag();
            if (nbt == null || !nbt.contains("type") || !nbt.contains("quality")) return;

            int qualityValue = nbt.getInt("quality");
            QualityEnum quality = QualityEnum.getQuality(qualityValue);
            if (quality == QualityEnum.UNDEFINED_QUALITY) return;

            String typeValue = nbt.getString("type");
            TypeEnum type = TypeEnum.getType(typeValue);
            if (type == TypeEnum.UNDEFINED_TYPE) return;

            ItemStack newItemStack = 
            ((DebrisItem) RegistryItem.EQUIPMENT_DEBRIS.get()).setDebrisTag(
                TypeEnum.UNDEFINED_TYPE,
                quality
            );

            event.setOutput(newItemStack);
            event.setXp(
                (quality == QualityEnum.DEFAULT_QUALITY ? 3 :quality.getNumber()) * 5
            );
        }
    }
}
