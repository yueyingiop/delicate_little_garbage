package com.core.DLG.util.BlockGui;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CartographyTableBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.SmithingTableBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGuiDetector {
    // 工作方块白名单
    private static final Set<Block> VANILLA_GUI_BLOCKS = Set.of(
        Blocks.CRAFTING_TABLE,
        Blocks.STONECUTTER,
        Blocks.ANVIL,
        Blocks.CHIPPED_ANVIL,
        Blocks.DAMAGED_ANVIL,
        Blocks.ENCHANTING_TABLE,
        Blocks.GRINDSTONE,
        Blocks.CARTOGRAPHY_TABLE,
        Blocks.LOOM,
        Blocks.SMITHING_TABLE,
        Blocks.FURNACE,
        Blocks.BLAST_FURNACE,
        Blocks.SMOKER
    );

    public static boolean hasGui(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        
        // 检查白名单
        if (VANILLA_GUI_BLOCKS.contains(block)) {
            return true;
        }
        
        // 检查方块实体是否有MenuProvider
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider) {
            return true;
        }
        
        // 检查方块类是否有GUI相关的方法（备用检测）
        return block instanceof CraftingTableBlock ||
               block instanceof StonecutterBlock ||
               block instanceof AnvilBlock ||
               block instanceof EnchantmentTableBlock ||
               block instanceof GrindstoneBlock ||
               block instanceof CartographyTableBlock ||
               block instanceof LoomBlock ||
               block instanceof SmithingTableBlock;
    }
    
    public static MenuProvider getMenuProvider(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        
        // 首先尝试从方块实体获取
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider) {
            return (MenuProvider) blockEntity;
        }
        
        // 对于原版方块，返回一个基于方块的MenuProvider
        return new BlockMenuProvider(block, state);
    }
}
