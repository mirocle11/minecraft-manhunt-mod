package com.sruproductions.manhuntmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.mojang.authlib.GameProfile;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class CustomPlayerEntity extends Player implements GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CustomPlayerEntity(EntityType<? extends Player> type, Level world, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(world, pos, yRot, gameProfile);
    }

    public CustomPlayerEntity(Level world, BlockPos pos, float yRot, GameProfile gameProfile) {
        this(EntityType.PLAYER, world, pos, yRot, gameProfile);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.custom_player.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }
}
