package com.core.DLG.block.entity.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.DLG.block.entity.CraftingBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class CraftingBlockEntityRender implements BlockEntityRenderer<CraftingBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public CraftingBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
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
            );
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(@Nonnull CraftingBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }
}
