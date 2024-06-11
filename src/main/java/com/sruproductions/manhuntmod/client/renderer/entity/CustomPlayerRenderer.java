package com.sruproductions.manhuntmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sruproductions.manhuntmod.client.renderer.entity.layers.CustomLayer;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomPlayerRenderer extends LivingEntityRenderer<CustomPlayerEntity, HumanoidModel<CustomPlayerEntity>> {

    private static final ResourceLocation CUSTOM_PLAYER_TEXTURE = new ResourceLocation("manhuntmod", "textures/entity/custom_player.png");

    public CustomPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new CustomLayer(this));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(CustomPlayerEntity entity) {
        return CUSTOM_PLAYER_TEXTURE;
    }

    @Override
    public void render(CustomPlayerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        // Additional custom rendering logic can be added here
    }
}
