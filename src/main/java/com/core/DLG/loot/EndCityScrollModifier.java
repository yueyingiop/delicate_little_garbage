package com.core.DLG.loot;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.core.DLG.item.RegistryItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class EndCityScrollModifier extends LootModifier {
    public static final Codec<EndCityScrollModifier> CODEC = RecordCodecBuilder.create(inst ->
        codecStart(inst).apply(inst, EndCityScrollModifier::new)
    );

    protected EndCityScrollModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
         return CODEC;
    }

    @Nonnull
    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
        ObjectArrayList<ItemStack> generatedLoot,
        LootContext context
    ) {
        if (context.getQueriedLootTableId().equals(new ResourceLocation("minecraft:chests/end_city_treasure"))) {
            generatedLoot.add(new ItemStack(RegistryItem.INDESTRUCTIBLE_SCROLL.get()));
        }
        return generatedLoot;
    }

}
