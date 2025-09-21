package com.core.DLG.events;

import java.util.List;
import java.util.Random;

import com.core.DLG.DLG;
import com.core.DLG.item.RegistryItem;
import com.core.DLG.util.CustomVillagerTrade;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesHandler {
    private static final Random RANDOM = new Random();
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) { 
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        
            List<VillagerTrades.ItemListing> level1Trades = trades.get(1);
            List<VillagerTrades.ItemListing> level5Trades = trades.get(5);
            if (level1Trades != null) {
                level1Trades.add(
                    new CustomVillagerTrade(
                        new ItemStack(Items.EMERALD, 1), 
                        new ItemStack(RegistryItem.NEPETA_CATARIA_SEEDS.get(), (RANDOM.nextInt(4) + 2)), 
                        8, 
                        2
                    )
                );
                level5Trades.add(
                    new CustomVillagerTrade(
                        new ItemStack(Items.EMERALD, (RANDOM.nextInt(17) + 16)), 
                        new ItemStack(RegistryItem.CLOUD_WHALE_SPAWN_EGG.get(), 1), 
                        1, 
                        8
                    )
                );
            }
        }
    }
}
