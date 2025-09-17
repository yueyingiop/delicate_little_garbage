package com.core.DLG.block.entity.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.DLG.block.entity.CraftingBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class CraftingBlockEntityRender implements BlockEntityRenderer<CraftingBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;
    private final ItemRenderer itemRenderer;

    public CraftingBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(
        @Nullable CraftingBlockEntity blockEntity, 
        float partialTick, 
        @Nullable PoseStack poseStack, 
        @Nullable MultiBufferSource buffer,
        int packedLight, int packedOverlay
    ) {
        if (blockEntity != null && poseStack != null && buffer != null) {
            poseStack.pushPose();
            BlockState blockState = blockEntity.getBlockState();
            blockRenderer.renderSingleBlock(
                blockState, 
                poseStack, 
                buffer, 
                packedLight,
                OverlayTexture.NO_OVERLAY,
                ModelData.EMPTY,
                RenderType.translucent()
            ); // 渲染方块

            ItemStack stack = blockEntity.getRenderStack();
            if (!stack.isEmpty()) {
                // 移动到方块中心上方
                poseStack.translate(0.5D, 1.0D + blockEntity.getHoverHeight(partialTick), 0.5D);
                // 旋转物品
                poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRotation(partialTick)));
                // 缩放物品大小
                poseStack.scale(0.8f, 0.8f, 0.8f);
                itemRenderer.renderStatic(
                    stack, 
                    ItemDisplayContext.GROUND, 
                    packedLight, 
                    packedOverlay, 
                    poseStack, 
                    buffer, 
                    blockEntity.getLevel(), 
                    0
                );
            }

            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(@Nonnull CraftingBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }
}
