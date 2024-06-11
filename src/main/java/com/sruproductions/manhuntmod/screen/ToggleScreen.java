package com.sruproductions.manhuntmod.screen;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import com.sruproductions.manhuntmod.screen.components.AbilityButton;
import com.sruproductions.manhuntmod.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToggleScreen extends Screen {
    private static final int BACKGROUND_WIDTH = 260;
    private static final int BACKGROUND_HEIGHT = 229;

    QuestProgress questProgress = QuestProgress.getInstance();
    private List<AbilityButton> abilityButtons;

    public ToggleScreen() {
        super(Component.literal("Toggle Screen"));
        loadQuestProgress();
    }

    private void loadQuestProgress() {
        try {
            questProgress.loadFromFile(ModResources.QUEST_PROGRESS_JSON);
        } catch (IOException e) {
            ManhuntMod.LOGGER.error("Failed to load quest progress", e);
        }
    }

    private void saveQuestProgress() {
        try {
            questProgress.saveToFile();
        } catch (IOException e) {
            ManhuntMod.LOGGER.error("Failed to save quest progress", e);
        }
    }

    @Override
    protected void init() {
        initAbilityButtons();

        int buttonWidth = 80;
        int buttonHeight = 20;
        int xPosition = this.width - buttonWidth - 10;
        int yPosition = this.height - buttonHeight - 10;

        this.addRenderableWidget(new Button.Builder(Component.literal("Keybinds"), button -> openKeyBindingScreen())
                .bounds(xPosition, yPosition, buttonWidth, buttonHeight)
                .build());
    }

    private void openKeyBindingScreen() {
        this.minecraft.setScreen(new KeyBindingScreen(this));
    }

    private void initAbilityButtons() {
        abilityButtons = new ArrayList<>();
        int buttonWidth = 30;
        int buttonHeight = 30;

        int startX = (width - BACKGROUND_WIDTH) / 2;
        int startY = (height - BACKGROUND_HEIGHT) / 2;

        int circleDiameter = 60;

        float[][] circlePositions = {
                {0.12f, 0.13f}, // Stage 1
                {0.65f, 0.13f}, // Stage 2
                {0.12f, 0.63f}, // Stage 3
                {0.65f, 0.63f}  // Stage 4
        };

        int positionIndex = 0;

        for (Stage stage : questProgress.getStages()) {
            if (positionIndex >= circlePositions.length) break;

            float[] circlePosition = circlePositions[positionIndex];
            int circleX = (int) (startX + circlePosition[0] * BACKGROUND_WIDTH);
            int circleY = (int) (startY + circlePosition[1] * BACKGROUND_HEIGHT);

            List<QuestProgress.Ability> abilities = stage.getAbilities();
            int numAbilities = abilities.size();
            int radius = circleDiameter / 2;

            // Center buttons horizontally within the circle
            int totalButtonWidth = numAbilities * buttonWidth + (numAbilities - 1) * 5;
            int buttonStartX = circleX + radius - totalButtonWidth / 2;
            int buttonY = circleY + radius - buttonHeight / 2;

            for (int i = 0; i < numAbilities; i++) {
                QuestProgress.Ability ability = abilities.get(i);
                int buttonX = buttonStartX + i * (buttonWidth + 5);

                boolean isLocked = !stage.isCompleted();
                AbilityButton abilityButton = new AbilityButton(buttonX, buttonY, buttonWidth, buttonHeight,
                        List.of(ability), isLocked);

                String abilityName = ability.getName().toLowerCase().replace(" ", "_");
                ResourceLocation abilityTexture = new ResourceLocation(ManhuntMod.MOD_ID,
                        "textures/abilities/" + abilityName + ".png");
                abilityButton.setTexture(abilityTexture);

                abilityButtons.add(abilityButton);
            }

            positionIndex++;
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        Minecraft.getInstance().getTextureManager().bindForSetup(ModResources.BACKGROUND_TEXTURE);
        int x = (this.width - BACKGROUND_WIDTH) / 2;
        int y = (this.height - BACKGROUND_HEIGHT) / 2;
        pGuiGraphics.blit(ModResources.BACKGROUND_TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH,
                BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        float[][] stagePositions = {
                {0.24f, 0.10f},
                {0.77f, 0.10f},
                {0.24f, 0.60f},
                {0.77f, 0.60f}
        };

        for (int i = 0; i < questProgress.getStages().size(); i++) {
            if (i >= stagePositions.length) break;

            Stage stage = questProgress.getStages().get(i);
            float[] stagePosition = stagePositions[i];
            int stageX = (int) (x + stagePosition[0] * BACKGROUND_WIDTH);
            int stageY = (int) (y + stagePosition[1] * BACKGROUND_HEIGHT);
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, stage.getName(), stageX, stageY, 0xFFFFFF);
        }

        for (AbilityButton abilityButton : abilityButtons) {
            abilityButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        for (AbilityButton abilityButton : abilityButtons) {
            if (abilityButton.isHovered()) {
                renderTooltip(pGuiGraphics, pMouseX, pMouseY, abilityButton.getAbilities());
            }
        }
    }

    private void renderTooltip(GuiGraphics pGuiGraphics, int mouseX, int mouseY, List<QuestProgress.Ability> abilities) {
        List<String> lines = getStrings(abilities);

        int tooltipWidth = lines.stream().mapToInt(line -> font.width(line)).max().orElse(0);
        int tooltipHeight = lines.size() * (font.lineHeight + 2);

        int tooltipX = mouseX + 12;
        int tooltipY = mouseY - 12;

        if (tooltipX + tooltipWidth > width) {
            tooltipX = mouseX - tooltipWidth - 12;
        }
        if (tooltipY + tooltipHeight > height) {
            tooltipY = mouseY - tooltipHeight - 12;
        }

        pGuiGraphics.fillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3,
                tooltipY + tooltipHeight + 3, -1073741824, -1073741824);

        float scale = 0.75f;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, 1.0F);
        for (int i = 0; i < lines.size(); i++) {
            pGuiGraphics.drawString(font, lines.get(i), (int) (tooltipX / scale),
                    (int) ((tooltipY + i * (font.lineHeight + 2)) / scale), 0xFFFFFF);
        }
        pGuiGraphics.pose().popPose();
    }

    private @NotNull List<String> getStrings(List<QuestProgress.Ability> abilities) {
        List<String> lines = new ArrayList<>();
        for (QuestProgress.Ability ability : abilities) {
            lines.add("Ability: " + ability.getName());
            lines.add("Description: " + ability.getDescription());

            for (Stage stage : questProgress.getStages()) {
                for (QuestProgress.Ability stageAbility : stage.getAbilities()) {
                    if (stageAbility.getName().equals(ability.getName())) {
                        lines.add("Quests:");
                        for (Quest quest : stage.getQuests()) {
                            String questStatus = quest.isCompleted() ? "Completed" : "Incomplete";
                            lines.add("- " + quest.getName() + " (" + questStatus + ")");
                        }
                    }
                }
            }
        }
        return lines;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_G) {
            this.minecraft.setScreen(null);
            saveQuestProgress();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
