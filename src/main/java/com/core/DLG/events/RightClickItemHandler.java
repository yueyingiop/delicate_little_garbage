package com.core.DLG.events;

import com.core.DLG.DLG;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
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

    // 切换Slot
    @SubscribeEvent
    public static void switchSlot(PlayerInteractEvent.RightClickItem event) { 
        Player player = event.getEntity();
        ItemStack handStack = event.getItemStack();
        CompoundTag tag = handStack.getTag();
        if (tag != null && tag.contains("AttributeModifiers") ) {
            ListTag modifiersList = tag.getList("AttributeModifiers", 10);
            // 玩家按下shift键
            if (player.isShiftKeyDown() && tag.contains("boundDebris")) {
                for (int i = 0; i < modifiersList.size(); i++) {
                    CompoundTag modifierTag = modifiersList.getCompound(i);
                    Component slot = Component.translatable("tips.dlg.solt.mainhand");
                    if (modifierTag.getString("Slot").equals("mainhand")) {
                        modifierTag.putString("Slot", "offhand");
                        slot = Component.translatable("tips.dlg.solt.offhand");
                    } else if (modifierTag.getString("Slot").equals("offhand")) {
                        modifierTag.putString("Slot", "mainhand");
                        slot = Component.translatable("tips.dlg.solt.mainhand");
                    }
                    player.displayClientMessage(Component.translatable("tips.dlg.solt.switch", slot).withStyle(ChatFormatting.GOLD), true);
                }
            }
        }
    
    }
}
