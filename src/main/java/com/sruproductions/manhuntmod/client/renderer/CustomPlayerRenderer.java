package com.sruproductions.manhuntmod.client.renderer;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.client.model.CustomPlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class CustomPlayerRenderer extends PlayerRenderer {

    public CustomPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, false);
        this.model = new CustomPlayerModel(context.bakeLayer(ModelLayers.PLAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "textures/entity/custom_player.png");
    }
}
