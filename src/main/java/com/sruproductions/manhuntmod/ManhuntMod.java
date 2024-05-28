package com.sruproductions.manhuntmod;

import com.mojang.logging.LogUtils;
import com.sruproductions.manhuntmod.overlay.QuestTrackerOverlay;
import com.sruproductions.manhuntmod.quest.QuestTracker;
import com.sruproductions.manhuntmod.screen.ToggleScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(ManhuntMod.MOD_ID)
public class ManhuntMod {
    public static final String MOD_ID = "manhuntmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static KeyMapping toggleScreenKey;

    public ManhuntMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeyMappings);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new QuestTracker());
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        QuestTrackerOverlay.init(event);
    }

    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        toggleScreenKey = new KeyMapping("key.manhuntmod.togglescreen", GLFW.GLFW_KEY_G, "key.categories.manhuntmod");
        event.register(toggleScreenKey);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server starting logic here
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (toggleScreenKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new ToggleScreen());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Additional client setup code here
        }
    }
}
