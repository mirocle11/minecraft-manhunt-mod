package com.sruproductions.manhuntmod.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;

public class CustomPlayerEntity extends Player {

    @ObjectHolder(registryName = "minecraft:entity_type", value = "manhuntmod:custom_player")
    public static final EntityType<CustomPlayerEntity> TYPE = null;

    public CustomPlayerEntity(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
