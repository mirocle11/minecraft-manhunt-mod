package com.sruproductions.manhuntmod.screen;

import com.sruproductions.manhuntmod.ManhuntMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyBindingScreen extends Screen {

    private final Screen parent;
    private final Map<String, EditBox> keyBindingInputs = new HashMap<>();
    private static final String[] ABILITIES = {
            "devour", "sonic_boom", "sculk_tentacles", "spider_aspect", "acid_orb", "starfall"
    };
    private static String currentAbility = null;

    protected KeyBindingScreen(Screen parent) {
        super(Component.literal("Key Bindings"));
        this.parent = parent;
        instance = this;
    }

    private static KeyBindingScreen instance;
    public static KeyBindingScreen getInstance() {
        return instance;
    }

    @Override
    protected void init() {
        int y = 50;

        for (String ability : ABILITIES) {
            EditBox input = new EditBox(this.font, this.width / 2 - 100, y, 200, 20, Component.literal(""));
            input.setValue(GLFW.glfwGetKeyName(ManhuntMod.getKeyBinding(ability).getKey().getValue(), 0));
            keyBindingInputs.put(ability, input);
            this.addRenderableWidget(input);

            y += 30;
        }

        this.addRenderableWidget(new Button.Builder(Component.literal("Save"), button -> saveKeyBindings())
                .bounds(this.width / 2 - 100, y, 200, 20)
                .build());

        this.addRenderableWidget(new Button.Builder(Component.literal("Cancel"), button -> onClose())
                .bounds(this.width / 2 - 100, y + 30, 200, 20)
                .build());
    }

    private void saveKeyBindings() {
        for (String ability : ABILITIES) {
            EditBox input = keyBindingInputs.get(ability);
            String keyName = input.getValue().toUpperCase();
            ManhuntMod.updateKeyBinding(ability, keyName);
        }
        onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title.getString(), this.width / 2, 15, 0xFFFFFF);

        int y = 50;
        for (String ability : ABILITIES) {
            guiGraphics.drawString(this.font, Component.literal(ability).getString(), this.width / 2 - 190, y + 5, 0xFFFFFF);
            y += 30;
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        if (currentAbility != null && Minecraft.getInstance().screen == null) {
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            for (int i = 32; i < 348; i++) {
                if (GLFW.glfwGetKey(windowHandle, i) == GLFW.GLFW_PRESS) {
                    int keyCode = GLFW.glfwGetKeyScancode(i);
                    instance.keyBindingInputs.get(currentAbility).setValue(GLFW.glfwGetKeyName(i, keyCode));
                    ManhuntMod.updateKeyBinding(currentAbility, GLFW.glfwGetKeyName(i, keyCode));
                    currentAbility = null;
                    Minecraft.getInstance().setScreen(instance);
                    break;
                }
            }
        }
    }
}
