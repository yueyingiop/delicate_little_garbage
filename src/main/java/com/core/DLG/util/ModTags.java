package com.core.DLG.util;

import com.core.DLG.DLG;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> BOTTLED_INGREDIENTS = 
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(DLG.MODID, "bottled_ingredients"));
    }
}
