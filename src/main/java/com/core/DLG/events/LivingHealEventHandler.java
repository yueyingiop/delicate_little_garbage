package com.core.DLG.events;

import com.core.DLG.DLG;
import com.core.DLG.attributes.RegistryAttribute;
import com.core.DLG.enums.DamageTypeEnum;
import com.core.DLG.util.damageText.DamageTextPacket;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHealEventHandler {

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity entity = event.getEntity();
        float healAmount = event.getAmount();        
        if (healAmount < 0.1f) return;
        AttributeInstance healingBonus = entity.getAttribute(RegistryAttribute.HEALING_BONUS.get());
        if (healingBonus != null) {
            healAmount += healingBonus.getValue();
        }
        event.setAmount(healAmount);
        
        DLG.NETWORK.send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
            new DamageTextPacket(entity.getId(), healAmount, DamageTypeEnum.HEALING.getId())
        );
    }
}
