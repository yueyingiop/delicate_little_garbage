package com.core.DLG.events;

import com.core.DLG.DLG;
import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.item.RegistryItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilRepairEventHandler {
    @SubscribeEvent
    public static void debrisFixEquipments(AnvilUpdateEvent event){
        ItemStack leftItemStack = event.getLeft();
        ItemStack rightItemStack = event.getRight();

        if (leftItemStack.isEmpty() || rightItemStack.isEmpty()) return;
        if (!rightItemStack.getItem().equals(RegistryItem.EQUIPMENT_DEBRIS.get())) return;
        if(!leftItemStack.isRepairable() || leftItemStack.getMaxDamage() == 0) return;

        // 判断是否损坏
        if (leftItemStack.isDamageableItem()) {
            // 检测品质存在
            CompoundTag nbt = rightItemStack.getTag();
            if (nbt == null || !nbt.contains("type") || !nbt.contains("quality")) return;

            // 类获取碎片类型
            String typeValue = nbt.getString("type");
            TypeEnum type = TypeEnum.getType(typeValue);

            // 获取碎片品质
            int qualityValue = nbt.getInt("quality");
            QualityEnum quality = QualityEnum.getQuality(qualityValue);
            if (quality == QualityEnum.UNDEFINED_QUALITY) return;

            // 获取物品类型
            TypeEnum leftItemType = null;
            QualityEnum leftItemQuality = null;
            String leftItemId = ForgeRegistries.ITEMS.getKey(leftItemStack.getItem()).toString();
            // 获取左物品的相关数据
            for (var tagkey : leftItemStack.getTags().toList()){
                Boolean isTrue = ItemConfig.itemInConfigList(leftItemId) || ItemConfig.itemInConfigList(tagkey.location().toString());
                if (isTrue) {
                    String name = ItemConfig.itemInConfigList(leftItemId) ? leftItemId : tagkey.location().toString();
                    leftItemType = TypeEnum.getType(ItemConfig.getConfig(name).get("type").getAsString());
                    leftItemQuality = QualityEnum.getQuality(ItemConfig.getConfig(name).get("quality").getAsInt());
                    break;
                }
            }
            if (leftItemType == null) return;
            if (leftItemQuality == null) return;

            if (leftItemType.getString() == type.getString() || type == TypeEnum.UNDEFINED_TYPE) {
                // 修复量(%)
                // 1 / 装备的品质 如果装备品质为 DEFAULT_QUALITY 则准备品质默认为3
                Float repairQuantity = 1.0F / (leftItemQuality == QualityEnum.DEFAULT_QUALITY ? 3 :leftItemQuality.getNumber());

                int maxDurability = leftItemStack.getMaxDamage(); // 物品最大耐久
                int currentDamage = leftItemStack.getDamageValue(); // 当前耐久
                // 修复的耐久
                // 如果装备碎片品质大于装备品质则回复全部耐久, 否则回复修复量(%)*碎片品质*装备最大耐久
                double repairRatio = 
                    (quality == QualityEnum.DEFAULT_QUALITY ? 3 :quality.getNumber()) >= leftItemQuality.getNumber() ? 
                    maxDurability: 
                    repairQuantity * (quality == QualityEnum.DEFAULT_QUALITY ? 3 :quality.getNumber()) * maxDurability;

                int newDamage = (int) Math.max(currentDamage - repairRatio, 0); // 新耐久
                ItemStack output = leftItemStack.copy();
                output.setDamageValue(newDamage);

                event.setOutput(output);
                event.setCost((int) Math.max(1, quality.getNumber() - 3));
                event.setMaterialCost(1);
            }

        }
    }
}
