package com.core.DLG.entity.client;

import javax.annotation.Nonnull;

import com.core.DLG.DLG;
import com.core.DLG.entity.CloudWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CloudWhaleRenderer extends MobRenderer<CloudWhaleEntity, CloudWhaleModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DLG.MODID,"textures/entity/cloud_whale.png");

    public CloudWhaleRenderer(EntityRendererProvider.Context context) {
        super(context, new CloudWhaleModel(context.bakeLayer(CloudWhaleModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(@Nonnull CloudWhaleEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(@Nonnull CloudWhaleEntity entity, @Nonnull PoseStack poseStack, float partialTickTime) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F); // 幼年体缩放0.5倍
        } else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
    }
}
