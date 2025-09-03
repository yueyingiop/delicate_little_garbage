package com.core.DLG.events;

import java.io.IOException;
import java.util.UUID;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemAttributeEventHandler {
    private static final UUID CRITICAL_CHANCE_UUID = UUID.fromString("9f072039-e911-42f0-9496-515d9762c33e");
    private static final UUID CRITICAL_DAMAGE_UUID = UUID.fromString("7c7235a2-1bbf-4c4c-bd8d-884950719446");

    // 为物品添加属性
    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) throws IOException {
        ItemConfig.init();
        if (!ItemConfig.getCustomC2C()) return; // 检测是否开启自定义双爆

        ItemStack itemStack = event.getItemStack();
        String itemId  = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
        for (var tagkey : itemStack.getTags().toList()) {
            Boolean isTrue = ItemConfig.itemInC2CItemList(itemId) || ItemConfig.itemInC2CItemList(tagkey.location().toString());
            if (isTrue && event.getSlotType() == EquipmentSlot.MAINHAND) {
                String name = ItemConfig.itemInC2CItemList(itemId) ? itemId : tagkey.location().toString();
                Double criticalChance = ItemConfig.getC2CItemConfig(name).get("criticalChance").getAsDouble();
                Double criticalDamage = ItemConfig.getC2CItemConfig(name).get("criticalDamage").getAsDouble();
                event.addModifier(
                    RegistryAttribute.CRITICAL_CHANCE.get(),
                    new AttributeModifier(
                        CRITICAL_CHANCE_UUID,
                        "dlg.critical_chance",
                        criticalChance,
                        Operation.ADDITION
                    )
                );
                event.addModifier(
                    RegistryAttribute.CRITICAL_DAMAGE.get(),
                    new AttributeModifier(
                        CRITICAL_DAMAGE_UUID,
                        "dlg.critical_damage",
                        criticalDamage,
                        Operation.ADDITION
                    )
                );
                return;
            }
        }
    }
}
