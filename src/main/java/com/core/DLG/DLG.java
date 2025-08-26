package com.core.DLG;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.core.DLG.configs.ItemConfig;
import com.core.DLG.item.RegistryItem;

@Mod(DLG.MODID)
public class DLG 
{

    public static final String MODID = "delicate_little_garbage";
    public static final Logger LOGGER = LogManager.getLogger(DLG.MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> DLG_TAB = CREATIVE_MODE_TABS.register("dlg_tab", () -> CreativeModeTab.builder()
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .title(Component.translatable("itemGroup.dlg_tab"))
        .icon(() -> RegistryItem.EQUIPMENT_DEBRIS.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(RegistryItem.EQUIPMENT_DEBRIS.get().getDefaultInstance());
        })
        .build()
    );


    public DLG(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        RegistryItem.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        
        
        MinecraftForge.EVENT_BUS.register(this);
        try {
            ItemConfig.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


    }
}
