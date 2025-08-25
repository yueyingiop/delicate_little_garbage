package com.core.DLG.mixin.itemStack;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.core.DLG.configs.ItemConfig;

import net.minecraft.world.Container;

@Mixin(Container.class)
public interface ContainerMixin {
    
    /**
     * @author yueyingiop
     * @reason 修改最大堆叠
     */
    @Overwrite
    default int getMaxStackSize() throws IOException {
        ItemConfig.init();
        return ItemConfig.getMaxStackSize();
    }
}
