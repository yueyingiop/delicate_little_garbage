package com.core.DLG.events;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;
import com.core.DLG.item.RegistryItem;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickBlockHandler {
    
    // 收集云朵方块
    @SubscribeEvent
    public static void collectCloudBlock(RightClickBlock event) { 
        // 玩家主手手持glass_bottle右键云朵方块将云朵方块收集变为云朵瓶
        boolean isMainHand = event.getHand().equals(InteractionHand.MAIN_HAND);
        if (isMainHand) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
                if (block == RegistryBlock.CLOUD_BLOCK.get()) {
                    if (!event.getEntity().isCreative()) {
                        itemStack.shrink(1);
                        ItemStack cloudBottle = new ItemStack(RegistryItem.CLOUD_BOTTLE.get());
                        if (!event.getEntity().getInventory().add(cloudBottle)) event.getEntity().spawnAtLocation(cloudBottle);
                    }

                    event.getLevel().removeBlock(event.getPos(), false);
                    event.setCanceled(true);
                }
            }
        }
    }

    // 使用云朵瓶
    @SubscribeEvent
    public static void useCloudBottle(RightClickBlock event) { 
        // 玩家主手手持云朵瓶右键方块防止云朵方块并返还玻璃瓶
        boolean isMainHand = event.getHand().equals(InteractionHand.MAIN_HAND);
        if (isMainHand) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getItem() == RegistryItem.CLOUD_BOTTLE.get()) { 
                BlockPos placePos = event.getPos().relative(event.getFace());
                BlockState blockState = RegistryBlock.CLOUD_BLOCK.get().defaultBlockState();
                if (event.getLevel().getBlockState(placePos).canBeReplaced()) {
                    event.getLevel().setBlock(placePos, blockState, 3);
                    event.getLevel().playSound(null, placePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                
                    if (!event.getEntity().isCreative()) {
                        itemStack.shrink(1);
                        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (!event.getEntity().getInventory().add(glassBottle)) {
                            event.getEntity().spawnAtLocation(glassBottle);
                        }
                    }

                    event.setCanceled(true);
                }
            }
        }
    }
}
