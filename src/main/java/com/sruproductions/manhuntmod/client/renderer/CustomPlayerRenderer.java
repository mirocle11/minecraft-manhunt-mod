package com.sruproductions.manhuntmod.client.renderer;

import com.sruproductions.manhuntmod.client.model.CustomPlayerModel;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomPlayerRenderer extends GeoEntityRenderer<CustomPlayerEntity> {

    public CustomPlayerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CustomPlayerModel());
    }
}
