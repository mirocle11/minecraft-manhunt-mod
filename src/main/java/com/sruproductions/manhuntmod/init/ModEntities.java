package com.sruproductions.manhuntmod.init;

import com.mojang.authlib.GameProfile;
import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.BlockPos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ManhuntMod.MOD_ID);

    public static final RegistryObject<EntityType<CustomPlayerEntity>> CUSTOM_PLAYER_ENTITY = ENTITY_TYPES.register("custom_player",
            () -> EntityType.Builder.<CustomPlayerEntity>of((type, world) -> new CustomPlayerEntity(world, BlockPos.ZERO, 0.0F, new GameProfile(null, "Player")),
                            MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .build("custom_player"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
