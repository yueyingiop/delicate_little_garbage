package com.core.DLG.init;

import com.core.DLG.DLG;
import com.core.DLG.block.RegistryBlock;
import com.core.DLG.block.entity.RegistryBlockEntity;
import com.core.DLG.block.entity.client.CraftingBlockEntityRender;
import com.core.DLG.inventory.CraftingBlockScreen;
import com.core.DLG.inventory.RegistryMenu;
import com.core.DLG.item.RegistryItem;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    // 根据物品品质显示图标
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //#region 注册装备碎片品质图标
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
            //#endregion

            // 设置方块渲染层透明
            ItemBlockRenderTypes.setRenderLayer(
                RegistryBlock.DEBRIS_SMITHING_TABLE.get(), 
                RenderType.translucent()
            );

            ItemBlockRenderTypes.setRenderLayer(
                RegistryBlock.NEPETA_CATARIA.get(), 
                RenderType.cutout()
            );

            // 设置gui渲染
            MenuScreens.register(
                RegistryMenu.CRAFTING_BLOCK_MENU.get(), 
                CraftingBlockScreen::new
            );


        });
    }

    // 注册方块模型
    @SubscribeEvent
    public static void registerRenderers(RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            RegistryBlockEntity.DEBRIS_SMITHING_TABLE_ENTITY.get(), 
            CraftingBlockEntityRender::new
        );
    }
}
