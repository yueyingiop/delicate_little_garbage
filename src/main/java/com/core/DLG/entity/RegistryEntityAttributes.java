package com.core.DLG.entity;

import com.core.DLG.DLG;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEntityAttributes {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
            RegistryEntity.CLOUD_WHALE.get(),
            CloudWhaleEntity.createAttributes().build()
        );
    }
}
