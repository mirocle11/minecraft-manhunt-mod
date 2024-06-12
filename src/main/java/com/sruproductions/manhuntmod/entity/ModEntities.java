package com.sruproductions.manhuntmod.entity;

import com.sruproductions.manhuntmod.ManhuntMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ManhuntMod.MOD_ID);

    public static final RegistryObject<EntityType<CustomPlayerEntity>> CUSTOM_PLAYER = ENTITIES.register("custom_player",
            () -> EntityType.Builder.of(ModEntities::createCustomPlayerEntity, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .build("custom_player"));

    private static CustomPlayerEntity createCustomPlayerEntity(EntityType<CustomPlayerEntity> type, Level world) {
        // Use default BlockPos, rotation, and GameProfile for now
        return new CustomPlayerEntity(type, world, BlockPos.ZERO, 0.0F, new GameProfile(null, "custom_player"));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        // Register entity attributes here if needed
    }
}
