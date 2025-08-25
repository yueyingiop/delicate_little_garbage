package com.core.DLG.mixin.itemStack;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void fixStacking(Level level, Player player, InteractionHand hand,CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        ArmorItem armor = (ArmorItem) (Object) this;
        EquipmentSlot slot = armor.getEquipmentSlot();

        if (stack.getCount() > 1) {
            // 取出1个盔甲
            ItemStack singleArmor = stack.split(1);

            // 取下原装备
            ItemStack oldArmor = player.getItemBySlot(slot);
            if (!oldArmor.isEmpty()) {
                // 尝试放入背包，放不下则丢地上
                if (!player.getInventory().add(oldArmor)) player.drop(oldArmor, false);
            }

            // 穿戴新装备
            player.setItemSlot(slot, singleArmor);

            cir.setReturnValue(InteractionResultHolder.sidedSuccess(stack, level.isClientSide()));
        }
    }
}
