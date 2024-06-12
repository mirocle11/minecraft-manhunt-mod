package com.sruproductions.manhuntmod.client.renderer;

import com.sruproductions.manhuntmod.client.model.CustomPlayerModel;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;

public class CustomPlayerRenderer extends PlayerRenderer {

    public CustomPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, false); // false for slim arms, true for default arms
        this.model = new CustomPlayerModel<>(context.bakeLayer(CustomPlayerModel.LAYER_LOCATION));
    }
}