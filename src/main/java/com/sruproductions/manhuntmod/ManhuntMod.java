package com.sruproductions.manhuntmod;

import com.mojang.logging.LogUtils;
import com.sruproductions.manhuntmod.data.QuestProgressManager;
import com.sruproductions.manhuntmod.network.NetworkHandler;
import com.sruproductions.manhuntmod.network.packet.CommandPacket;
import com.sruproductions.manhuntmod.overlay.QuestTrackerOverlay;
import com.sruproductions.manhuntmod.quest.QuestTracker;
import com.sruproductions.manhuntmod.screen.ToggleScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
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
    private static KeyMapping toggleQuestTrackerOverlayKey;
    public static KeyMapping castDevourKey;
    public static KeyMapping castSonicBoomKey;
    public static KeyMapping castSculkTentacles;
    public static KeyMapping castSpiderAspect;
    public static KeyMapping castAcidOrb;
    public static KeyMapping castStarfall;

    public ManhuntMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeyMappings);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new QuestTracker());
        NetworkHandler.register();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        QuestTrackerOverlay.init(event);
    }

    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        toggleScreenKey = new KeyMapping("key.manhuntmod.togglescreen", GLFW.GLFW_KEY_G, "key.categories.manhuntmod");
        event.register(toggleScreenKey);
        toggleQuestTrackerOverlayKey = new KeyMapping("key.manhuntmod.togglequesttrackeroverlay", GLFW.GLFW_KEY_Y, "key.categories.manhuntmod");
        event.register(toggleQuestTrackerOverlayKey);
        castDevourKey = new KeyMapping("key.manhuntmod.castdevour", GLFW.GLFW_KEY_Z, "key.categories.manhuntmod");
        event.register(castDevourKey);
        castSonicBoomKey = new KeyMapping("key.manhuntmod.castsonic_boom", GLFW.GLFW_KEY_X, "key.categories.manhuntmod");
        event.register(castSonicBoomKey);
        castSculkTentacles = new KeyMapping("key.manhuntmod.castsculk_tentacles", GLFW.GLFW_KEY_C, "key.categories.manhuntmod");
        event.register(castSculkTentacles);
        castSpiderAspect = new KeyMapping("key.manhuntmod.castspider_aspect", GLFW.GLFW_KEY_V, "key.categories.manhuntmod");
        event.register(castSpiderAspect);
        castAcidOrb = new KeyMapping("key.manhuntmod.castacid_orb", GLFW.GLFW_KEY_B, "key.categories.manhuntmod");
        event.register(castSpiderAspect);
        castStarfall = new KeyMapping("key.manhuntmod.caststarfall", GLFW.GLFW_KEY_N, "key.categories.manhuntmod");
        event.register(castStarfall);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Set the server instance and load quest data when server starts
        QuestProgressManager.getInstance().setServer(event.getServer());
        QuestTracker questTracker = QuestTracker.getInstance();
        questTracker.loadQuestProgress();
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        // Save quest data when server stops
        QuestProgressManager.getInstance().setServer(event.getServer());
        QuestTracker questTracker = QuestTracker.getInstance();
        questTracker.saveQuestProgress();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (toggleScreenKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new ToggleScreen());
            }
        }
        if (toggleQuestTrackerOverlayKey.consumeClick()) {
            QuestTrackerOverlay.toggleVisibility();
        }
        if (castDevourKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s devour 4"));
            }
        }
        if (castSonicBoomKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s sonic_boom 4"));
            }
        }
        if (castSculkTentacles.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s sculk_tentacles 4"));
            }
        }
        if (castSpiderAspect.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s spider_aspect 4"));
            }
        }
        if (castAcidOrb.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s acid_orb 4"));
            }
        }
        if (castStarfall.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player instanceof LocalPlayer) {
                NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s starfall 4"));
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
