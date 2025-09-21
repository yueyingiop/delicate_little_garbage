package com.core.DLG.util.damageText;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class DamageTextPacket {
    private final int entityId;
    private final double damage;
    private final int damageTypeId;

    public DamageTextPacket(int entityId, double damage, int damageTypeId) {
        this.entityId = entityId;
        this.damage = damage;
        this.damageTypeId = damageTypeId;
    }

    public static void encode(DamageTextPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeDouble(msg.damage);
        buffer.writeInt(msg.damageTypeId);
    }
    
    public static DamageTextPacket decode(FriendlyByteBuf buffer) {
        return new DamageTextPacket(
            buffer.readInt(),
            buffer.readDouble(),
            buffer.readInt()
        );
    }

    public static void handle(DamageTextPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handleClient(msg);
        });
        ctx.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private static void handleClient(DamageTextPacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && minecraft.level != null) {
            Entity entity = minecraft.level.getEntity(msg.entityId);
            if (entity instanceof LivingEntity living) {
                DamageTextManager.spawnDamageText(
                    minecraft.level,
                    living,
                    msg.damage,
                    msg.damageTypeId
                );
            }
        }
    }
}
