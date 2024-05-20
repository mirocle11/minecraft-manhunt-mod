package com.sruproductions.manhuntmod.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;

public class ToggleScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ManhuntMod.MOD_ID,
            "textures/gui/gui_prototype2.png");
    private static final ResourceLocation ABILITY_TEXTURE = new ResourceLocation(ManhuntMod.MOD_ID,
            "textures/gui/ability_icon.png");
    private static final ResourceLocation DEFAULT_CONFIG_RESOURCE = new ResourceLocation(ManhuntMod.MOD_ID,
            "config/quest_progress.json");

    private boolean isToggled = false;
    private static final int BACKGROUND_WIDTH = 260;
    private static final int BACKGROUND_HEIGHT = 229;
    private Button button;

    private final QuestProgress questProgress;
    private final File saveFile;

    public ToggleScreen() {
        super(Component.literal("Toggle Screen"));
        this.questProgress = new QuestProgress();
        this.saveFile = new File(Minecraft.getInstance().gameDirectory, "quest_progress.json");
        loadQuestProgress();
    }

    private void loadQuestProgress() {
        try {
            questProgress.loadFromFile(saveFile, DEFAULT_CONFIG_RESOURCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveQuestProgress() {
        try {
            questProgress.saveToFile(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() {
        int buttonWidth = 80;
        int buttonHeight = 20;
        int x = (width - buttonWidth) / 2;
        int y = (height - buttonHeight) / 2;

        this.button = addRenderableWidget(Button.builder(Component.literal("Toggle"), button -> {
            isToggled = !isToggled;
            button.setMessage(Component.literal(isToggled ? "Toggle On" : "Toggle Off"));
        }).pos(x, y).size(buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Call the superclass render method to retain base rendering functionality
        renderBackground(pGuiGraphics);

        Minecraft.getInstance().getTextureManager().bindForSetup(BACKGROUND_TEXTURE);
        int x = (this.width - BACKGROUND_WIDTH) / 2;
        int y = (this.height - BACKGROUND_HEIGHT) / 2;
        pGuiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT,
                BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        drawStageLabels(pGuiGraphics, x, y, pMouseX, pMouseY);

        // Add your custom rendering code here
        // For example, you might want to draw additional components or text
        pGuiGraphics.drawString(this.font, "Custom Screen", 20, 20, 0xFFFFFF);
    }

    private void drawStageLabels(GuiGraphics pGuiGraphics, int startX, int startY, int mouseX, int mouseY) {
        // Define the positions of the circles
        int circleDiameter = 60;
        int circlePadding = 20;

        int[][] circlePositions = {
                { startX + 30, startY },
                { startX + 90 + circleDiameter + circlePadding, startY },
                { startX + 30, startY + 35 + circleDiameter + circlePadding },
                { startX + 90 + circleDiameter + circlePadding, startY + 35 + circleDiameter + circlePadding }
        };

        int index = 0;
        for (Stage stage : questProgress.getStages()) {
            if (index >= circlePositions.length) break;

            int[] position = circlePositions[index];
            int textX = position[0] + (circleDiameter / 2) - (this.font.width(stage.getName()) / 2);
            int textY = position[1] + (circleDiameter / 2) - 4; // Adjust Y to vertically center the text
            pGuiGraphics.drawString(this.font, stage.getName(), textX, textY, 0xFFFFFF);

            // Draw quests
            int questStartY = textY + 20;
            for (Quest quest : stage.getQuests()) {
                pGuiGraphics.pose().pushPose(); // Save the current transformation matrix
                pGuiGraphics.pose().scale(0.60f, 0.60f, 1.0f); // Scale down the text size
                pGuiGraphics.drawString(this.font, quest.getName(), (int) (textX / 0.60f) - 30,
                        (int) (questStartY / 0.60f), 0xFFFFFF);
                pGuiGraphics.pose().popPose(); // Restore the original transformation matrix
                questStartY += 10;
            }

            // Draw ability
            int abilityIconY = questStartY + 2;
            Minecraft.getInstance().getTextureManager().bindForSetup(ABILITY_TEXTURE);
            pGuiGraphics.blit(ABILITY_TEXTURE, position[0] + (circleDiameter / 2) - 8, abilityIconY,
                    0, 0, 16, 16, 16, 16);

            if (isMouseOver(mouseX, mouseY, position[0] + (circleDiameter / 2) - 8, abilityIconY, 16)) {
                String abilityInfo = stage.isCompleted() ? "Ability Gained" : "Ability Pending";
                int tooltipWidth = this.font.width(abilityInfo);
                int tooltipX = mouseX + 12;
                int tooltipY = mouseY - 12;

                pGuiGraphics.fillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY +
                        8 + 3, -1073741824, -1073741824);
                pGuiGraphics.drawString(this.font, abilityInfo, tooltipX, tooltipY, 0xFFFFFF);
            }

            index++;
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int size) {
        return mouseX >= x && mouseY >= y && mouseX < x + size && mouseY < y + size;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_G) {
            this.minecraft.setScreen(null); // Close the screen when ESC is pressed
            saveQuestProgress(); // Save progress when exiting the screen
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
