package com.sruproductions.manhuntmod.client.model;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.resources.ResourceLocation;

public class CustomPlayerModel extends PlayerModel<AbstractClientPlayer> implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CustomPlayerModel(ModelPart root) {
        super(root, false); // Assuming you are not using slim model, pass true if you are.
    }

    public ResourceLocation getModelResource() {
        return new ResourceLocation("manhuntmod", "geo/custom_player_model.geo.json");
    }

    public ResourceLocation getTextureResource() {
        return new ResourceLocation("manhuntmod", "textures/entity/custom_player.png");
    }

    public ResourceLocation getAnimationResource() {
        return new ResourceLocation("manhuntmod", "animations/custom_player.animation.json");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }
}
