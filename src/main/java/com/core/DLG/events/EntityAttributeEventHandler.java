package com.core.DLG.events;

import java.io.IOException;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeEventHandler {

    // 为玩家添加属性
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent  event)  throws IOException  {
        ItemConfig.init();
        if (ItemConfig.getCustomAttribute()) {
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.CRITICAL_CHANCE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.CRITICAL_DAMAGE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.DODGE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.PENETRATION_CHANCE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.PENETRATION_DAMAGE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.LIFESTEAL_CHANCE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.LIFESTEAL_DAMAGE.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.HP_REGEN.get());
            detectAndAdd(event, EntityType.PLAYER, RegistryAttribute.HEALING_BONUS.get());
        }
    }
    public static void detectAndAdd(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType, Attribute attribute){
        if (!event.has(entityType, attribute)) event.add(entityType, attribute);
    }

    
}
