package com.core.DLG.events;

import com.core.DLG.DLG;
// import com.core.DLG.attributes.RegistryAttribute;

// import net.minecraft.world.entity.LivingEntity;
// import net.minecraft.world.entity.ai.attributes.AttributeInstance;
// import net.minecraft.world.entity.player.Player;
// import net.minecraftforge.event.TickEvent;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DLG.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickEventHandler {
    // private static final java.util.Map<LivingEntity, Integer> lastHealTickMap = new java.util.WeakHashMap<>();

    // @SubscribeEvent
    // public static void onServerTick(TickEvent.PlayerTickEvent event) {
    //     if (event.phase != TickEvent.Phase.END) return;
    //     int currentTick = event.player.tickCount;
    //     processEntityRegeneration(event.player, currentTick);
    // }

    // private static void processEntityRegeneration(Player entity, int currentTick) {
    //     if (!entity.isAlive() || entity.isSpectator()) return;

    //     AttributeInstance hpRegenAttr = entity.getAttribute(RegistryAttribute.HP_REGEN.get());
    //     if (hpRegenAttr == null) return;

    //     double hpRegenValue = hpRegenAttr.getValue();
    //     if (hpRegenValue <= 0) return;

    //     int lastHealTick = lastHealTickMap.getOrDefault(entity, -1000);
    //     if (currentTick - lastHealTick < 20) return;

    //     float maxHealth = entity.getMaxHealth();
    //     float currentHealth = entity.getHealth();
    //     if ((currentHealth+hpRegenValue) >= maxHealth) {
    //         hpRegenValue = maxHealth - currentHealth;
    //     };

    //     if (currentTick - lastHealTick >= 20) {
    //         entity.heal((float)hpRegenValue);
    //         lastHealTickMap.put(entity, currentTick);
    //     }
    // }
}
