package com.sruproductions.manhuntmod;

import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ManhuntMod.MOD_ID);
    public static final RegistryObject<EntityType<CustomPlayerEntity>> CUSTOM_PLAYER = ENTITY_TYPES.register("custom_player",
            () -> EntityType.Builder.<CustomPlayerEntity>of((type, world) -> new CustomPlayerEntity(world,
                    BlockPos.ZERO, 0.0F, new GameProfile(null, "CustomPlayer")),
                    MobCategory.MISC).sized(0.6F, 1.8F).build(new ResourceLocation(ManhuntMod.MOD_ID,
                    "custom_player").toString()));

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<EntityType<?>>()
                .setName(new ResourceLocation(ManhuntMod.MOD_ID, "entity"))
                .setDefaultKey(new ResourceLocation(ManhuntMod.MOD_ID, "custom_player"))
                .allowModification());
    }
}


