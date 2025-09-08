package com.core.DLG.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class CraftingBlockItem extends BlockItem {
    private final String itemId;

    public CraftingBlockItem(String itemId, Block block, Properties properties) {
        super(block, properties);
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
