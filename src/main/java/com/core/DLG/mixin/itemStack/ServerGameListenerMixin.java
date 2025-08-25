package com.core.DLG.mixin.itemStack;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.core.DLG.configs.ItemConfig;

import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGameListenerMixin {
    @ModifyConstant(method = "handleSetCreativeModeSlot",constant = @Constant(intValue = 64))
    public int action(int constant) throws IOException {
        ItemConfig.init();
        return ItemConfig.getMaxStackSize();
    }
}
