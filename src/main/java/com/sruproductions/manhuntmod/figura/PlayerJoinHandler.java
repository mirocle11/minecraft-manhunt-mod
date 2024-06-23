package com.sruproductions.manhuntmod.figura;

import com.sruproductions.manhuntmod.ManhuntMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraAPIManager;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import net.minecraft.client.player.LocalPlayer;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ManhuntMod.MOD_ID, value = Dist.CLIENT)
public class PlayerJoinHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) {
            initializeDefaultAvatar((LocalPlayer) event.getEntity());
        }
    }

    private static void initializeDefaultAvatar(LocalPlayer player) {
        // Create or load the default avatar
        Avatar defaultAvatar = loadDefaultAvatar(player);

        // Setup the Lua runtime with the default avatar and an empty script map
        Map<String, String> scripts = new HashMap<>();
        FiguraLuaRuntime runtime = new FiguraLuaRuntime(defaultAvatar, scripts);

        // Setup types and APIs
        FiguraAPIManager.setupTypesAndAPIs(runtime);

        // Set the default avatar
        runtime.setGlobal("avatar", defaultAvatar);
    }

    private static Avatar loadDefaultAvatar(LocalPlayer player) {
        // Use the constructor that accepts an Entity
        return new Avatar(player);
    }
}
