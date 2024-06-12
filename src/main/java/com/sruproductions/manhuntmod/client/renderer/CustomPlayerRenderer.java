package com.sruproductions.manhuntmod.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity; // Correct import
import com.sruproductions.manhuntmod.client.model.CustomPlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class CustomPlayerRenderer extends GeoEntityRenderer<CustomPlayerEntity> {

    public CustomPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, new CustomPlayerModel());
    }

    @Override
    public RenderType getRenderType(CustomPlayerEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
