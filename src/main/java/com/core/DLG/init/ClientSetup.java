package com.core.DLG.init;

import com.core.DLG.DLG;
import com.core.DLG.item.RegistryItem;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    // 根据物品品质显示图标
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                RegistryItem.EQUIPMENT_DEBRIS.get(), 
                ResourceLocation.fromNamespaceAndPath(DLG.MODID, "quality"), 
                (stack, world, entity, seed)->{
                    if (stack.hasTag()) {
                        CompoundTag nbt = stack.getTag();
                        if (nbt==null) return 1.0F;
                        if (nbt.contains("quality")) {
                            int quality = nbt.getInt("quality");
                            switch (quality) {
                                case 0: return 0.3F; // 默认品质
                                // 1到9的品质
                                case 1: return 0.1F;
                                case 2: return 0.2F;
                                case 3: return 0.3F;
                                case 4: return 0.4F;
                                case 5: return 0.5F;
                                case 6: return 0.6F;
                                case 7: return 0.7F;
                                case 8: return 0.8F;
                                case 9: return 0.9F;
                                
                                default: return 1.0F; // 未定义的品质
                            }
                        }
                    }
                    return 1.0F;
                }
            );
        });
    }
}
