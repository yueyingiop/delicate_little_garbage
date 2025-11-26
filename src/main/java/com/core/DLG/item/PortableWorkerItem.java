package com.core.DLG.item;

import javax.annotation.Nonnull;

import com.core.DLG.util.BlockGui.BlockGuiDetector;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class PortableWorkerItem extends Item {
    public PortableWorkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        // 检测Shift+右键方块
        if (player.isShiftKeyDown()) {
            return this.bindToBlock(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            InteractionResult result = this.openBoundGui(level, player, hand);
            return new InteractionResultHolder<>(result, player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private InteractionResult bindToBlock(UseOnContext context) {
        Level level = context.getLevel(); // 获取当前世界
        BlockPos pos = context.getClickedPos(); // 获取点击的方块位置
        BlockState state = level.getBlockState(pos); // 获取点击的方块状态
        Player player = context.getPlayer(); // 获取点击的玩家
        if (player == null) return InteractionResult.PASS;
        ItemStack stack = context.getItemInHand(); // 获取点击的玩家物品

        if (BlockGuiDetector.hasGui(level, pos, state)) { 
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
            stack.getOrCreateTag().putString("BoundBlock", blockId.toString()); // 绑定方块ID到物品NBT
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("Bound to: " + state.getBlock().getName()), true);
            }
            return InteractionResult.SUCCESS;
        } else {
            // 该方块没有GUI
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("This block has no GUI!"), true);
            }
            return InteractionResult.FAIL;
        }
    }

    private InteractionResult openBoundGui(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getTag();
        // 检查是否有绑定信息
        if (tag == null || !tag.contains("BoundBlock")) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("No block bound!"), true);
            }
            return InteractionResult.FAIL;
        }
        
        String boundBlockId = tag.getString("BoundBlock");
        // 在服务器端尝试打开GUI
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            Block targetBlock = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(boundBlockId));
            
            if (targetBlock != null) {
                // 创建对应的菜单提供者
                BlockState blockState = targetBlock.defaultBlockState();
                MenuProvider menuProvider = BlockGuiDetector.getMenuProvider(level, BlockPos.ZERO, blockState);
                
                if (menuProvider != null) {
                    serverPlayer.openMenu(menuProvider);
                    return InteractionResult.SUCCESS;
                }
            }
            
            player.displayClientMessage(Component.literal("无法打开绑定的方块GUI!"), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
