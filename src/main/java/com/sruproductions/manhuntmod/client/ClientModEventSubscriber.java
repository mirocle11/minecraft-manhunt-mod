package com.sruproductions.manhuntmod.client;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.client.renderer.entity.CustomPlayerRenderer;
import com.sruproductions.manhuntmod.entity.CustomPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CustomPlayerEntity.TYPE, CustomPlayerRenderer::new);
    }
}
