package com.sruproductions.manhuntmod;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.sruproductions.manhuntmod.commands.ResetCommand;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.network.NetworkHandler;
import com.sruproductions.manhuntmod.network.packet.CommandPacket;
import com.sruproductions.manhuntmod.overlay.QuestTrackerOverlay;
import com.sruproductions.manhuntmod.quest.QuestTracker;
import com.sruproductions.manhuntmod.screen.ToggleScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(ManhuntMod.MOD_ID)
public class ManhuntMod {
    public static final String MOD_ID = "manhuntmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static KeyMapping toggleScreenKey;
    private static KeyMapping toggleQuestTrackerOverlayKey;
    private static final Map<String, KeyMapping> keyBindings = new HashMap<>();
    public static KeyMapping castDevourKey;
    public static KeyMapping castSonicBoomKey;
    public static KeyMapping castSculkTentacles;
    public static KeyMapping castSpiderAspect;
    public static KeyMapping castAcidOrb;
    public static KeyMapping castStarfall;
    QuestProgress questProgress = QuestProgress.getInstance();

    public ManhuntMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(this::registerKeyMappings);
            MinecraftForge.EVENT_BUS.register(this);
            MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
            MinecraftForge.EVENT_BUS.register(new QuestTracker());
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        QuestTrackerOverlay.init(event);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ResetCommand.register(event.getDispatcher());
    }

    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        toggleScreenKey = new KeyMapping("key.manhuntmod.togglescreen", GLFW.GLFW_KEY_H, "key.categories.manhuntmod");
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
        event.register(castAcidOrb);
        castStarfall = new KeyMapping("key.manhuntmod.caststarfall", GLFW.GLFW_KEY_N, "key.categories.manhuntmod");
        event.register(castStarfall);

        keyBindings.put("devour", castDevourKey);
        keyBindings.put("sonic_boom", castSonicBoomKey);
        keyBindings.put("sculk_tentacles", castSculkTentacles);
        keyBindings.put("spider_aspect", castSpiderAspect);
        keyBindings.put("acid_orb", castAcidOrb);
        keyBindings.put("starfall", castStarfall);
    }

    public static KeyMapping getKeyBinding(String abilityName) {
        return keyBindings.get(abilityName);
    }

    public static void updateKeyBinding(String abilityName, String keyName) {
        KeyMapping keyMapping = keyBindings.get(abilityName);
        if (keyMapping != null) {
            int keyCode = mapCharacterToGLFWKey(keyName.charAt(0));
            if (keyCode != -1) {
                keyMapping.setKey(InputConstants.getKey(keyCode, 0));
                KeyMapping.resetMapping();
            }
        }
    }

    private static int mapCharacterToGLFWKey(char character) {
        switch (character) {
            case 'A': return GLFW.GLFW_KEY_A;
            case 'B': return GLFW.GLFW_KEY_B;
            case 'C': return GLFW.GLFW_KEY_C;
            case 'D': return GLFW.GLFW_KEY_D;
            case 'E': return GLFW.GLFW_KEY_E;
            case 'F': return GLFW.GLFW_KEY_F;
            case 'G': return GLFW.GLFW_KEY_G;
            case 'H': return GLFW.GLFW_KEY_H;
            case 'I': return GLFW.GLFW_KEY_I;
            case 'J': return GLFW.GLFW_KEY_J;
            case 'K': return GLFW.GLFW_KEY_K;
            case 'L': return GLFW.GLFW_KEY_L;
            case 'M': return GLFW.GLFW_KEY_M;
            case 'N': return GLFW.GLFW_KEY_N;
            case 'O': return GLFW.GLFW_KEY_O;
            case 'P': return GLFW.GLFW_KEY_P;
            case 'Q': return GLFW.GLFW_KEY_Q;
            case 'R': return GLFW.GLFW_KEY_R;
            case 'S': return GLFW.GLFW_KEY_S;
            case 'T': return GLFW.GLFW_KEY_T;
            case 'U': return GLFW.GLFW_KEY_U;
            case 'V': return GLFW.GLFW_KEY_V;
            case 'W': return GLFW.GLFW_KEY_W;
            case 'X': return GLFW.GLFW_KEY_X;
            case 'Y': return GLFW.GLFW_KEY_Y;
            case 'Z': return GLFW.GLFW_KEY_Z;
            case '0': return GLFW.GLFW_KEY_0;
            case '1': return GLFW.GLFW_KEY_1;
            case '2': return GLFW.GLFW_KEY_2;
            case '3': return GLFW.GLFW_KEY_3;
            case '4': return GLFW.GLFW_KEY_4;
            case '5': return GLFW.GLFW_KEY_5;
            case '6': return GLFW.GLFW_KEY_6;
            case '7': return GLFW.GLFW_KEY_7;
            case '8': return GLFW.GLFW_KEY_8;
            case '9': return GLFW.GLFW_KEY_9;
            default: return -1;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
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
            if (questProgress.isAbilityUnlocked("sonic_boom")) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player instanceof LocalPlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s sonic_boom 4"));
                }
            } else {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("You haven't learned this spell yet"));
                }
            }
        }
        if (castSculkTentacles.consumeClick()) {
            if (questProgress.isAbilityUnlocked("sculk_tentacles")) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player instanceof LocalPlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s sculk_tentacles 4"));
                }
            } else {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("You haven't learned this spell yet"));
                }
            }
        }
        if (castSpiderAspect.consumeClick()) {
            if (questProgress.isAbilityUnlocked("spider_aspect")) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player instanceof LocalPlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s spider_aspect 4"));
                }
            } else {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("You haven't learned this spell yet"));
                }
            }
        }
        if (castAcidOrb.consumeClick()) {
            if (questProgress.isAbilityUnlocked("acid_orb")) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player instanceof LocalPlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s acid_orb 4"));
                }
            } else {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("You haven't learned this spell yet"));
                }
            }
        }
        if (castStarfall.consumeClick()) {
            if (questProgress.isAbilityUnlocked("starfall")) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player instanceof LocalPlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/cast @s starfall 4"));
                }
            } else {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("You haven't learned this spell yet"));
                }
            }
        }
    }
}
