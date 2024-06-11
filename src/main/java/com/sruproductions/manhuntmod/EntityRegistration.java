package com.sruproductions.manhuntmod;

import com.mojang.authlib.GameProfile;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistration {

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        EntityType<CustomPlayerEntity> customPlayerEntityType = EntityType.Builder
                .<CustomPlayerEntity>of((type, world) -> new CustomPlayerEntity(world, BlockPos.ZERO, 0, new GameProfile(null, "CustomPlayer")), MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .build("custom_player");

        customPlayerEntityType.setRegistryName(ManhuntMod.MODID, "custom_player");

        event.getRegistry().register(customPlayerEntityType);
    }
}
