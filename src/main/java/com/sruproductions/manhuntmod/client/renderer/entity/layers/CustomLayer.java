package com.sruproductions.manhuntmod.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CustomLayer extends RenderLayer<CustomPlayerEntity, HumanoidModel<CustomPlayerEntity>> {

    private static final ResourceLocation CUSTOM_LAYER_TEXTURE = new ResourceLocation("manhuntmod", "textures/entity/custom_layer.png");

    public CustomLayer(RenderLayerParent<CustomPlayerEntity, HumanoidModel<CustomPlayerEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CustomPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType(CUSTOM_LAYER_TEXTURE));
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private RenderType renderType(ResourceLocation customLayerTexture) {
        return null;
    }
}
