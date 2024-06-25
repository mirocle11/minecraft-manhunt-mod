package com.sruproductions.manhuntmod.client;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.client.renderer.CustomPlayerRenderer;
import com.sruproductions.manhuntmod.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventSubscriber {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Register your custom player renderer
        event.registerEntityRenderer(ModEntities.CUSTOM_PLAYER.get(), CustomPlayerRenderer::new);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        // Replace the default player renderer with your custom renderer
        EntityRenderers.register(ModEntities.CUSTOM_PLAYER.get(), CustomPlayerRenderer::new);
    }
}
