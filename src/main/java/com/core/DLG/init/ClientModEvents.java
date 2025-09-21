package com.core.DLG.init;

import com.core.DLG.DLG;
import com.core.DLG.entity.RegistryEntity;
import com.core.DLG.entity.client.CloudWhaleModel;
import com.core.DLG.entity.client.CloudWhaleRenderer;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    // 注册模型
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CloudWhaleModel.LAYER_LOCATION, CloudWhaleModel::createBodyLayer);
    }

    // 注册渲染器
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(RegistryEntity.CLOUD_WHALE.get(), CloudWhaleRenderer::new);
    }

    
}
