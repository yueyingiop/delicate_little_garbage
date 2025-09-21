package com.core.DLG.entity.client;

import javax.annotation.Nonnull;

import com.core.DLG.DLG;
import com.core.DLG.entity.CloudWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class CloudWhaleModel extends HierarchicalModel<CloudWhaleEntity> {
// public class CloudWhaleModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DLG.MODID, "cloud_whale_model"), "main");
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart bone;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;

	public CloudWhaleModel(ModelPart root) {
		this.root = root;
		this.main = root.getChild("main");
		this.bone = this.main.getChild("bone");
		this.bone5 = this.bone.getChild("bone5");
		this.bone6 = this.bone.getChild("bone6");
		this.bone2 = this.main.getChild("bone2");
		this.bone3 = this.bone2.getChild("bone3");
		this.bone4 = this.bone2.getChild("bone4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -1.5708F, 1.5708F, -1.5708F));

		PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 12).addBox(-9.0F, -1.0F, -3.5F, 18.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -6.0F, -3.5F, 19.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-6.0F, -8.0F, -3.5F, 15.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-6.0F, -9.0F, -3.5F, 11.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(-9.0F, -6.0F, -4.5F, 18.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 37).addBox(-7.0F, -7.0F, -4.5F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 35).addBox(-9.0F, -6.0F, 3.5F, 18.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 39).addBox(-7.0F, -7.0F, 3.5F, 15.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 41).addBox(-8.0F, -7.0F, -3.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(18, 45).addBox(-3.1667F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(46, 45).addBox(-5.1667F, -1.0F, -4.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-5.1667F, -1.0F, 1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.8333F, -3.0F, 0.0F));

		PartDefinition bone6 = bone.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(50, 15).addBox(0.0F, -0.8F, -1.0F, 2.0F, 0.8F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(42, 50).addBox(0.1F, -1.6F, -0.9F, 1.8F, 0.8F, 1.8F, new CubeDeformation(0.0F))
		.texOffs(50, 50).addBox(0.2F, -2.4F, -0.8F, 1.6F, 0.8F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(26, 66).addBox(0.3F, -3.2F, -0.7F, 1.4F, 0.8F, 1.4F, new CubeDeformation(0.0F))
		.texOffs(36, 62).addBox(0.4F, -4.0F, -0.6F, 1.2F, 0.8F, 1.2F, new CubeDeformation(0.0F))
		.texOffs(12, 49).addBox(0.5F, -4.8F, -0.5F, 1.0F, 0.8F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 18).addBox(0.65F, -5.6F, -0.35F, 0.7F, 0.8F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition bone2 = main.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(18, 41).addBox(-4.0F, -1.0F, -2.5F, 7.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(38, 41).addBox(-5.0F, -2.0F, -2.5F, 7.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(44, 20).addBox(-6.0F, -3.0F, -2.5F, 7.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 1.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(44, 24).addBox(-2.25F, 0.0F, -2.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 49).addBox(-2.25F, 0.0F, -3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 5.0F, -5.5F, 0.5236F, 0.0F, 0.0F));

		PartDefinition bone4 = bone2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(32, 45).addBox(-2.25F, 0.0F, 0.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 12).addBox(-2.25F, 0.0F, 2.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 5.0F, 3.5F, -0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(
		@Nonnull CloudWhaleEntity entity, 
		float limbSwing, 
		float limbSwingAmount, 
		float ageInTicks,
		float netHeadYaw, 
		float headPitch
	) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (entity.moveAnimationState.isStarted()) {
			this.animate(entity.moveAnimationState, CloudWhaleAnimation.move, ageInTicks);
		}
		if (entity.jumpAnimationState.isStarted()) {
			this.animate(entity.jumpAnimationState, CloudWhaleAnimation.jump, ageInTicks);
		}
	}

	@Override
	public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}