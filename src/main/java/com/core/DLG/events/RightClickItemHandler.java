package com.core.DLG.events;

import com.core.DLG.DLG;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickItemHandler {
    // 穿戴物品
    @SubscribeEvent
    public static void wearItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack handStack = event.getItemStack();
        CompoundTag tag = handStack.getTag();
        if (tag != null && tag.contains("boundDebris") ) {
            CompoundTag debrisTag = tag.getCompound("boundDebris");
            ItemStack currentArmor = null;
            EquipmentSlot slot = null;
            switch (debrisTag.getString("type")) {
                case "helmet":
                    slot = EquipmentSlot.HEAD;
                    break;
                case "chestplate":
                    slot = EquipmentSlot.CHEST;
                    break;
                case "leggings":
                    slot = EquipmentSlot.LEGS;
                    break;
                case "boots":
                    slot = EquipmentSlot.FEET;
                    break;
                default:
                    return;
            }
            if (slot != null) currentArmor = player.getItemBySlot(slot);
            if(!currentArmor.isEmpty()) 
                if (!player.getInventory().add(currentArmor)) player.drop(currentArmor, false);
            ItemStack copy = handStack.copy();
            copy.setCount(1);
            player.setItemSlot(slot, copy);
            handStack.shrink(1);
            event.setCanceled(true);
        }
    }
}
