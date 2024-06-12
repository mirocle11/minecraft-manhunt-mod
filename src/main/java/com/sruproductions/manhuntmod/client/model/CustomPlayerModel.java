package com.sruproductions.manhuntmod.client.model;

import com.sruproductions.manhuntmod.ManhuntMod;
import software.bernie.geckolib.model.GeoModel;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.resources.ResourceLocation;

public class CustomPlayerModel extends GeoModel<CustomPlayerEntity> {

    @Override
    public ResourceLocation getModelResource(CustomPlayerEntity entity) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "geo/custom_player_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomPlayerEntity entity) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "textures/entity/custom_player.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomPlayerEntity entity) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "animations/custom_player.animation.json");
    }
}
