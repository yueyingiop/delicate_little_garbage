package com.core.DLG.events;

import java.util.UUID;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
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
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event){
        String itemId  = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem()).toString();
        if (itemId.equals("minecraft:netherite_sword") && event.getSlotType() == EquipmentSlot.MAINHAND) {
            event.addModifier(
                RegistryAttribute.CRITICAL_CHANCE.get(),
                new AttributeModifier(
                    CRITICAL_CHANCE_UUID,
                    "dlg.critical_chance",
                    0.15D,
                    Operation.ADDITION
                )
            );
            event.addModifier(
                RegistryAttribute.CRITICAL_DAMAGE.get(),
                new AttributeModifier(
                    CRITICAL_DAMAGE_UUID,
                    "dlg.critical_damage",
                    0.5D,
                    Operation.ADDITION
                )
            );
        }
    }
}
