package com.sruproductions.manhuntmod.client;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.client.renderer.CustomPlayerRenderer;
import com.sruproductions.manhuntmod.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.entity.EntityRenderers;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventSubscriber {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_PLAYER.get(), CustomPlayerRenderer::new);
    }
}
