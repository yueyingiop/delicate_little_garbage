package com.core.DLG.mixin.itemStack;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.core.DLG.configs.ItemConfig;

import java.io.IOException;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @ModifyConstant(method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",constant = @Constant(intValue = 64))
    private static int action(int constant) throws IOException {
        ItemConfig.init();
        return ItemConfig.getMaxStackSize();
    }
}
