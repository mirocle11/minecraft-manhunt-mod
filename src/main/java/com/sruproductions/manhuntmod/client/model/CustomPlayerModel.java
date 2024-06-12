package com.sruproductions.manhuntmod.client.model;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CustomPlayerModel extends GeoModel<CustomPlayerEntity> {

    @Override
    public ResourceLocation getModelResource(CustomPlayerEntity animatable) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "geo/custom_player_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomPlayerEntity animatable) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "textures/entity/custom_player.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomPlayerEntity animatable) {
        return new ResourceLocation(ManhuntMod.MOD_ID, "animations/custom_player.animation.json");
    }
}
