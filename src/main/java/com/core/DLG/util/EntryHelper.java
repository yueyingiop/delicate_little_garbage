package com.core.DLG.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class EntryHelper {
    private static final Random RANDOM = new Random();

    // 初始化词条
    public static void initEntry(CompoundTag itemTag, TypeEnum typeEnum, QualityEnum qualityEnum) throws IOException {
        ItemConfig.init();
        JsonObject entrySetting = ItemConfig.getConfigInEntrySetting(qualityEnum.getNumber());
        JsonArray entryList = getEntryList(typeEnum);
        List<JsonObject> selectedEntries = selectRandomEntries(entryList,entrySetting.get("basicEntriesCount").getAsInt());
    
        ListTag modifiersList = itemTag.contains("AttributeModifiers", 9)?itemTag.getList("AttributeModifiers", 10):new ListTag();
        for (JsonObject entry : selectedEntries) { 
            CompoundTag modifierTag = new CompoundTag();
            modifierTag.putString("AttributeName", entry.get("attribute").getAsString());
            Double baseValue = entry.get("baseValue").getAsDouble();
            Double baseRange = entry.get("baseRange").getAsDouble();
            Double amount = baseValue + (RANDOM.nextDouble(baseRange*2) - baseRange);
            modifierTag.putDouble("Amount", amount);
            modifierTag.putInt("Operation", entry.get("operation").getAsInt());
            modifierTag.putIntArray("UUID", generateUUIDIntArray());
            modifierTag.putString("Slot", getSolt(typeEnum));
            modifierTag.putInt("upgradeCount", 0);
            modifiersList.add(modifierTag);
        }
        itemTag.put("AttributeModifiers", modifiersList);
        CompoundTag entryTag = new CompoundTag();
        entryTag.putInt("entryCount", entrySetting.get("basicEntriesCount").getAsInt());
        entryTag.putInt("maxEntryCount", entrySetting.get("maxEntriesCount").getAsInt());
        entryTag.putInt("upgradeCount", 0);
        itemTag.put("entry", entryTag);
    }

    // 迁移默认属性
    public static void transitDefaultAttribute(ItemStack itemStack, CompoundTag itemTag) {
        ListTag modifiersList = itemTag.contains("AttributeModifiers", 9)?itemTag.getList("AttributeModifiers", 10):new ListTag();
        for (EquipmentSlot eSlot:EquipmentSlot.values()) {
            itemStack.getItem().getDefaultAttributeModifiers(eSlot).forEach((attribute, modifier) -> {
                CompoundTag modifierTag = new CompoundTag();
                CompoundTag defaultModifierTag = modifier.save();
                modifierTag.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(attribute).toString());
                Double amount = defaultModifierTag.getDouble("Amount");
                if (amount<0 && attribute.getDescriptionId() == "attribute.name.generic.attack_speed") amount = 4+amount;
                modifierTag.putDouble("Amount", amount);
                modifierTag.putInt("Operation", defaultModifierTag.getInt("Operation"));
                modifierTag.putIntArray("UUID", generateUUIDIntArray());
                modifierTag.putString("Slot", eSlot.getName());
                modifiersList.add(modifierTag);
            });
        }
        itemTag.put("AttributeModifiers", modifiersList);
    }

    // 升级词条
    public static void upgradeEntry(CompoundTag itemTag) {
        // 物品绑定信息
        CompoundTag debrisTag = itemTag.getCompound("boundDebris");
        // 词条信息
        CompoundTag entryTag = itemTag.getCompound("entry");
        // 词条列表(TAG)
        ListTag modifiersTag = itemTag.contains("AttributeModifiers", 9)?itemTag.getList("AttributeModifiers", 10):new ListTag();
        // 过滤词条列表(LIST)
        List<CompoundTag> modifiersList = new ArrayList<>();
        modifiersTag.forEach((tag)->{
            CompoundTag modifierTag = (CompoundTag)tag;
            if (modifierTag.contains("upgradeCount")) {
                modifiersList.add(modifierTag);
            }
        });

        int level = debrisTag.getInt("level");
        int upgradeCount = entryTag.getInt("upgradeCount");
        int maxEntryCount = entryTag.getInt("maxEntryCount");
        int entryCount = entryTag.getInt("entryCount");
        int upgradeLevel = (int)(level/5);
        if (upgradeLevel == 0) return;
        if (upgradeLevel == upgradeCount) return;

        int temp = 0;
        // 补充剩余词条
        if (entryCount < maxEntryCount) {
            temp = entryCount + upgradeLevel - maxEntryCount;
            TypeEnum typeEnum = TypeEnum.getType(debrisTag.getString("type"));
            JsonArray entryList = getEntryList(typeEnum);
            List<JsonObject> allEntryList = selectRandomEntries(entryList,entryList.size());
            for (CompoundTag modifierTag : modifiersList) {
                removeEntry(
                    allEntryList,
                    modifierTag.getString("AttributeName"),
                    modifierTag.getInt("Operation")
                );
            }

            for (int i = 0; i < (maxEntryCount-entryCount); i++) { 
                CompoundTag modifierTag = new CompoundTag();
                JsonObject entry = allEntryList.get(i);
                modifierTag.putString("AttributeName", entry.get("attribute").getAsString());
                Double baseValue = entry.get("baseValue").getAsDouble();
                Double baseRange = entry.get("baseRange").getAsDouble();
                Double amount = baseValue + (RANDOM.nextDouble(baseRange*2) - baseRange);
                modifierTag.putDouble("Amount", amount);
                modifierTag.putInt("Operation", entry.get("operation").getAsInt());
                modifierTag.putIntArray("UUID", generateUUIDIntArray());
                modifierTag.putString("Slot", getSolt(typeEnum));
                modifierTag.putInt("upgradeCount", 0);
                modifiersTag.add(modifierTag);
                modifiersList.add(modifierTag);
            }
            itemTag.put("AttributeModifiers", modifiersTag);

            entryTag.putInt("entryCount", temp>0?maxEntryCount:maxEntryCount+temp);
        }

        // 如果词条补充完全,并且temp为0,重新计算
        if (upgradeLevel != upgradeCount && temp == 0) {
            temp = upgradeLevel - upgradeCount;
        }
        // 词条升级
        if (temp>0) {
            for (int i = 0; i < temp; i++) { 
                CompoundTag modifierTag = (CompoundTag)modifiersTag.get(RANDOM.nextInt(modifiersList.size()));
                List<JsonObject> availableEntries = new ArrayList<>();
                for (JsonElement element: ItemConfig.getAttackType()) {
                    availableEntries.add(element.getAsJsonObject());
                }
                for (JsonElement element: ItemConfig.getDefenseType()) {
                    availableEntries.add(element.getAsJsonObject());
                }
                for (JsonElement element: ItemConfig.getCustomType()) {
                    availableEntries.add(element.getAsJsonObject());
                }
                for (JsonObject entry: availableEntries) {
                    if (
                        entry.get("attribute").getAsString().equals(modifierTag.getString("AttributeName")) && 
                        entry.get("operation").getAsInt() == modifierTag.getInt("Operation") &&
                        modifierTag.contains("upgradeCount")

                    ){
                        Double updateValue = entry.get("updateValue").getAsDouble();
                        Double updateRange = entry.get("updateRange").getAsDouble();
                        Double amount = updateValue + (RANDOM.nextDouble(updateRange*2) - updateRange);
                        modifierTag.putDouble("Amount", modifierTag.getDouble("Amount")+amount);
                        modifierTag.putInt("upgradeCount", modifierTag.getInt("upgradeCount")+1);
                        break;
                    }
                }
            }
        }

        entryTag.putInt("upgradeCount", upgradeLevel);
    }

    // 获取词条列表
    private static JsonArray getEntryList(TypeEnum typeEnum) {
        switch (typeEnum) {
            case SWORD:
                return ItemConfig.getAttackType();
            case SHOVEL:
                return ItemConfig.getCustomType();
            case PICKAXE:
                return ItemConfig.getCustomType();
            case AXE:
                return ItemConfig.getCustomType();
            case HOE:
                return ItemConfig.getCustomType();
            case BOW:
                return ItemConfig.getAttackType();
            case CROSSBOW:
                return ItemConfig.getAttackType();
            case TRIDENT:
                return ItemConfig.getAttackType();
            case HELMET:
                return ItemConfig.getDefenseType();
            case CHESTPLATE:
                return ItemConfig.getDefenseType();
            case LEGGINGS:
                return ItemConfig.getDefenseType();
            case BOOTS:
                return ItemConfig.getDefenseType();
            default:
                return ItemConfig.getCustomType();
        }
    }

    // 从entryList中抽取不重复词条
    private static List<JsonObject> selectRandomEntries(JsonArray entryList, int count) {
        List<JsonObject> result = new ArrayList<>();
        List<JsonObject> availableEntries = new ArrayList<>();
        
        // 将JsonArray转换为List
        for (JsonElement element : entryList) {
            availableEntries.add(element.getAsJsonObject());
        }
        
        // 确保不超出可用词条数量
        count = Math.min(count, availableEntries.size());
        
        // 随机抽取不重复词条
        for (int i = 0; i < count; i++) {
            int randomIndex = RANDOM.nextInt(availableEntries.size());
            result.add(availableEntries.get(randomIndex));
            availableEntries.remove(randomIndex);
        }
        
        return result;
    }

    // 从List<JsonObject>词条中删除指定attribute的词条
    public static void removeEntry(List<JsonObject> entryList, String attribute, int operation) {
        for (int i = entryList.size() - 1; i >= 0; i--) {
            JsonObject entry = entryList.get(i);
            if (entry.get("attribute").getAsString().equals(attribute) && entry.get("operation").getAsInt() == operation) {
                entryList.remove(i);
            }
        }
    }

    // 获取装备类型对应的solt
    private static String getSolt(TypeEnum typeEnum) {
        switch (typeEnum) {
            case SWORD:
                return EquipmentSlot.MAINHAND.getName();
            case SHOVEL:
                return EquipmentSlot.MAINHAND.getName();
            case PICKAXE:
                return EquipmentSlot.MAINHAND.getName();
            case AXE:
                return EquipmentSlot.MAINHAND.getName();
            case HOE:
                return EquipmentSlot.MAINHAND.getName();
            case BOW:
                return EquipmentSlot.MAINHAND.getName();
            case CROSSBOW:
                return EquipmentSlot.MAINHAND.getName();
            case TRIDENT:
                return EquipmentSlot.MAINHAND.getName();
            case HELMET:
                return EquipmentSlot.HEAD.getName();
            case CHESTPLATE:
                return EquipmentSlot.CHEST.getName();
            case LEGGINGS:
                return EquipmentSlot.LEGS.getName();
            case BOOTS:
                return EquipmentSlot.FEET.getName();
            default:
                return EquipmentSlot.OFFHAND.getName();
        }
    }

    // 生成随机UUID
    private static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    // 将UUID转换为int数组
    private static int[] uuidToIntArray(UUID uuid) {
        long mostSignificant = uuid.getMostSignificantBits();
        long leastSignificant = uuid.getLeastSignificantBits();
        return new int[] {
            (int)(mostSignificant >> 32),
            (int)mostSignificant,
            (int)(leastSignificant >> 32),
            (int)leastSignificant
        };
    }

    // 生成随机UUID(int数组)
    private static int[] generateUUIDIntArray() {
        UUID uuid = UUID.randomUUID();
        return uuidToIntArray(uuid);
    }
    
}
