package com.core.DLG.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class CustomVillagerTrade implements VillagerTrades.ItemListing {
    private final ItemStack currencyItemStack; // 货币
    private final ItemStack goodsItemStack; // 商品
    private final int maxUses; // 使用次数
    private final int villagerXp; // 经验
    private final float priceMultiplier; // 价格倍数
    private final boolean sell; // 是否是出售


    // 默认买入
    public CustomVillagerTrade(
        ItemStack currencyItemStack, 
        ItemStack goodsItemStack, 
        int maxUses, 
        int villagerXp
    ) { 
        this(currencyItemStack, goodsItemStack, maxUses, villagerXp, 0.05f, false);
    }

    public CustomVillagerTrade(ItemStack currencyItemStack, ItemStack goodsItemStack, int maxUses, int villagerXp, float priceMultiplier, boolean sell) {
        this.currencyItemStack = currencyItemStack;
        this.goodsItemStack = goodsItemStack;
        this.maxUses = maxUses;
        this.villagerXp = villagerXp;
        this.priceMultiplier = 0.05F;
        this.sell = false;
    }

    @Override
    @Nullable
    public MerchantOffer getOffer(@Nonnull Entity trader,@Nonnull RandomSource random) {
        if (sell) {
            // 出售
            return new MerchantOffer(
                this.goodsItemStack,
                this.currencyItemStack,
                this.maxUses,
                this.villagerXp,
                priceMultiplier
            );
        } else {
            // 购买
            return new MerchantOffer(
                this.currencyItemStack,
                this.goodsItemStack,
                this.maxUses,
                this.villagerXp,
                priceMultiplier
            );
        }
    }

}
