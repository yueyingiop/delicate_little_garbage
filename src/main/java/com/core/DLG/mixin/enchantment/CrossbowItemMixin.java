package com.core.DLG.mixin.enchantment;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.core.DLG.configs.EnchantmentConfig;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Invoker("addChargedProjectile")
    private static void invokeAddChargedProjectile(ItemStack crossbowStack, ItemStack projectile) {
        throw new AssertionError("Mixin invoker not implemented");
    }


    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack modifyGetProjectile(Player player, ItemStack weapon) throws IOException {
        EnchantmentConfig.init();
        ItemStack projectile = player.getProjectile(weapon); // 获取武器的弹丸
        
        // 检查武器是否有无限附魔
        int infinityLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, weapon);
        if (infinityLevel > 0 && EnchantmentConfig.getInfinityCrossbow()) {
            boolean isTrulyInfinite = EnchantmentConfig.getTrulyInfinite();
            if (isTrulyInfinite || !projectile.isEmpty()) {
                return new ItemStack(Items.ARROW);
            }
        }
        
        return projectile;
    }

    // /**
    //  * @author - yueyingoiop
    //  * @reason 修改加载箭的逻辑v1
    //  * @param entity - 实体
    //  * @param crossbow - 弩
    //  * @param projectile - 箭
    //  * @param simulated - 模拟
    //  * @param creative - 是否为创造
    //  * @return - 是否成功加载
    // */
    // @Overwrite
    // private static boolean loadProjectile(LivingEntity entity, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) throws IOException {
    //     EnchantmentConfig.init();
    //     int infinity = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbow);
    //     boolean isEmpty = projectile.isEmpty();
    //     ItemStack itemStack;
    //     if (infinity > 0 && EnchantmentConfig.getInfinityCrossbow() && entity instanceof Player) {
    //         boolean isTrulyInfinite = EnchantmentConfig.getTrulyInfinite();
    //         if (isTrulyInfinite || !isEmpty) {
    //             itemStack = isTrulyInfinite ? new ItemStack(Items.ARROW) : projectile.copy();
    //             invokeAddChargedProjectile(crossbow, itemStack);
    //             return true;
    //         }
    //     }
    //     if (isEmpty) {
    //         return false;
    //     } else {
    //         boolean flag = creative && projectile.getItem() instanceof ArrowItem;
    //         if (!flag&&!creative&&!simulated) {
    //             itemStack = projectile.split(1);
    //             if (projectile.isEmpty() && entity instanceof Player) {
    //                 ((Player)entity).getInventory().removeItem(projectile);
    //             }
    //         } else {
    //             itemStack = projectile.copy();
    //         }
    // 
    //         invokeAddChargedProjectile(crossbow,itemStack);
    //         return true;
    //     }
    // }

    /**
     * @author - yueyingoiop
     * @reason 修改加载箭的逻辑v2
    */
    @Inject(method = "loadProjectile", at = @At("HEAD"), cancellable = true)
    private static void onLoadProjectile(LivingEntity entity, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative, CallbackInfoReturnable<Boolean> cir) throws IOException {
        EnchantmentConfig.init();
        int infinity = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbow);
        boolean isEmpty = projectile.isEmpty();
        ItemStack itemStack;
        if (infinity > 0 && EnchantmentConfig.getInfinityCrossbow() && entity instanceof Player) {
            boolean isTrulyInfinite = EnchantmentConfig.getTrulyInfinite();
            if (isTrulyInfinite || !isEmpty) {
                itemStack = isTrulyInfinite ? new ItemStack(Items.ARROW) : projectile.copy();
                invokeAddChargedProjectile(crossbow, itemStack);
                cir.setReturnValue(true);
            }
        }
    }

    /**
     * 注入方法，在弩发射箭矢时修改箭矢的拾取状态
     * 当弩具有无限附魔时，设置箭矢为不可拾取
     */
    @Inject(
        method = "shootProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void onShootProjectile(
        Level level, 
        LivingEntity shooter, 
        InteractionHand hand, 
        ItemStack crossbow, 
        ItemStack projectile, 
        float soundPitch, 
        boolean isCreativeMode, 
        float velocity, 
        float inaccuracy, 
        float projectileAngle, 
        CallbackInfo ci,
        boolean flag, 
        Projectile projectileEntity
    ) {
        // 检查弩是否有无限附魔
        int infinityLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, crossbow);
        if (infinityLevel > 0 && EnchantmentConfig.getInfinityCrossbow()) {
            if (projectileEntity instanceof AbstractArrow) {
                AbstractArrow arrow = (AbstractArrow) projectileEntity;
                arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            }
        }
    }
}
