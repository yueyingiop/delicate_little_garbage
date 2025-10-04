package com.core.DLG;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.block.RegistryBlock;
import com.core.DLG.block.entity.RegistryBlockEntity;
import com.core.DLG.configs.ItemConfig;
import com.core.DLG.effect.RegistryEffect;
import com.core.DLG.effect.RegistryPotion;
import com.core.DLG.entity.RegistryEntity;
import com.core.DLG.generation.RegistryFeature;
import com.core.DLG.generation.data.WorldGenProvider;
import com.core.DLG.inventory.RegistryMenu;
import com.core.DLG.item.RegistryItem;
import com.core.DLG.loot.RegistryGLM;
import com.core.DLG.recipe.RegistryRecipe;
import com.core.DLG.util.damageHUD.DamageHUDPacket;
import com.core.DLG.util.damageText.DamageTextPacket;

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
            output.accept(RegistryItem.DELICATE_LITTLE_GARBAGE.get());
            output.accept(RegistryItem.LIFT_CRYSTAL.get());
            output.accept(RegistryItem.INDESTRUCTIBLE_SCROLL.get());
            output.accept(RegistryItem.NEPETA_CATARIA_ITEM.get());
            output.accept(RegistryItem.NEPETA_CATARIA_LEAF.get());
            output.accept(RegistryItem.NEPETA_CATARIA_SEEDS.get());
            output.accept(RegistryItem.DEBRIS_SMITHING_TABLE_ITEM.get());
            output.accept(RegistryItem.CLOUD_SUGAR.get());
            output.accept(RegistryItem.CLOUD_WHALE_SPAWN_EGG.get());
            output.accept(RegistryItem.CLOUD_BLOCK_ITEM.get());
            output.accept(RegistryItem.CLOUD_BOTTLE.get());
        })
        .build()
    );

    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(MODID, "main"),
        () -> "1.0",
        s -> true,
        s -> true
    );


    public DLG(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        RegistryItem.ITEMS.register(modEventBus); // 注册物品
        RegistryMenu.MENUS.register(modEventBus); // 注册菜单
        RegistryBlockEntity.BLOCK_ENTITIES.register(modEventBus); // 注册方块实体
        RegistryBlock.BLOCKS.register(modEventBus); // 注册方块
        RegistryEntity.ENTITIES.register(modEventBus); // 注册实体
        RegistryEffect.EFFECTS.register(modEventBus); // 注册效果
        RegistryPotion.POTIONS.register(modEventBus); // 注册药水
        RegistryGLM.GLM.register(modEventBus); // 注册战利品
        RegistryAttribute(modEventBus); // 注册属性
        RegistryFeature.FEATURES.register(modEventBus); // 注册生成规则
        RegistryRecipe.RECIPE_SERIALIZERS.register(modEventBus); // 注册自定义序列化器
        CREATIVE_MODE_TABS.register(modEventBus); // 注册创造模式标签

        modEventBus.addListener(this::setupNetwork);
        modEventBus.addListener(this::gatherData);
        MinecraftForge.EVENT_BUS.register(this);
        try {
            ItemConfig.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void RegistryAttribute(IEventBus modEventBus){
        try {
            ItemConfig.init();
            if (ItemConfig.getCustomAttribute()) {
                RegistryAttribute.ATTRIBUTES.register(modEventBus);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void setupNetwork(final FMLCommonSetupEvent event) {
        int id = 0;
        NETWORK.registerMessage(
            id++, 
            DamageTextPacket.class, 
            DamageTextPacket::encode, 
            DamageTextPacket::decode, 
            DamageTextPacket::handle
        );
        NETWORK.registerMessage(
            id++, 
            DamageHUDPacket.class, 
            DamageHUDPacket::encode, 
            DamageHUDPacket::decode, 
            DamageHUDPacket::handle
        );
    }

    private void gatherData(GatherDataEvent event) {
    PackOutput output = event.getGenerator().getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
    
    event.getGenerator().addProvider(event.includeServer(), new WorldGenProvider(output, lookupProvider));
}
}
