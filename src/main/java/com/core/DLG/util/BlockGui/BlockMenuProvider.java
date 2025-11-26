package com.core.DLG.util.BlockGui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.DLG.block.RegistryBlock;
import com.core.DLG.inventory.CraftingBlockMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMenuProvider implements MenuProvider {
    private final Block block;
    private final BlockState state;
    
    public BlockMenuProvider(Block block, BlockState state) {
        this.block = block;
        this.state = state;
    }

    @Override
    public Component getDisplayName() {
        return block.getName();
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        // 根据方块类型返回对应的菜单
        if (block == Blocks.CRAFTING_TABLE) {
            return new CraftingMenu(containerId, playerInventory);
        } else if (block == Blocks.STONECUTTER) {
            return new StonecutterMenu(containerId, playerInventory);
        } else if (block == Blocks.ANVIL || block == Blocks.CHIPPED_ANVIL || block == Blocks.DAMAGED_ANVIL) {
            return new AnvilMenu(containerId, playerInventory);
        } else if (block == Blocks.ENCHANTING_TABLE) {
            return new EnchantmentMenu(containerId, playerInventory);
        } else if (block == Blocks.GRINDSTONE) {
            return new GrindstoneMenu(containerId, playerInventory);
        } else if (block == Blocks.CARTOGRAPHY_TABLE) {
            return new CartographyTableMenu(containerId, playerInventory);
        } else if (block == Blocks.LOOM) {
            return new LoomMenu(containerId, playerInventory);
        } else if (block == Blocks.SMITHING_TABLE) {
            return new SmithingMenu(containerId, playerInventory);
        } else if (block == Blocks.FURNACE) {
            return new FurnaceMenu(containerId, playerInventory);
        } else if (block == Blocks.BLAST_FURNACE) {
            return new FurnaceMenu(containerId, playerInventory);
        } else if (block == Blocks.SMOKER) {
            return new FurnaceMenu(containerId, playerInventory);
        } else if (block == RegistryBlock.DEBRIS_SMITHING_TABLE.get()) {
            return new CraftingBlockMenu(containerId, playerInventory);
        }
        
        return null;
    }

    public Block getBlock() {
        return block;
    }

    public BlockState getState() {
        return state;
    }

}
