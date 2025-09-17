package com.core.DLG.util.damageHUD;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DamageHUDPacket {
    private UUID playerId;
    private double damage;
    private long lastDamageTime;

    public DamageHUDPacket(UUID playerId, double damage, long lastDamageTime) {
        this.playerId = playerId;
        this.damage = damage;
        this.lastDamageTime = lastDamageTime;
    }

    public static void encode(DamageHUDPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(msg.playerId);
        buffer.writeDouble(msg.damage);
        buffer.writeLong(msg.lastDamageTime);
    }

    public static DamageHUDPacket decode(FriendlyByteBuf buffer) {
        return new DamageHUDPacket(
            buffer.readUUID(),
            buffer.readDouble(),
            buffer.readLong()
        );
    }

    public static void handle(DamageHUDPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (
                    minecraft != null && 
                    minecraft.level != null && 
                    minecraft.player != null && 
                    minecraft.player.getUUID().equals(msg.playerId)
                ) {
                    DamageHUDManager.updateDamage(msg.damage, msg.lastDamageTime);
                }
                
            }
            
        });
        ctx.get().setPacketHandled(true);
    }

}
