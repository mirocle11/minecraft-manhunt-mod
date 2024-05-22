package com.sruproductions.manhuntmod.screen;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import com.sruproductions.manhuntmod.screen.components.AbilityButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToggleScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ManhuntMod.MOD_ID, "textures/gui/gui_prototype2.png");

    private static final int BACKGROUND_WIDTH = 260;
    private static final int BACKGROUND_HEIGHT = 229;

    private final QuestProgress questProgress;
    private final File saveFile;
    private List<AbilityButton> abilityButtons;

    public ToggleScreen() {
        super(Component.literal("Toggle Screen"));
        this.questProgress = new QuestProgress();
        this.saveFile = new File(Minecraft.getInstance().gameDirectory, "quest_progress.json");
        loadQuestProgress();
    }

    private void loadQuestProgress() {
        try {
            questProgress.loadFromFile(saveFile, new ResourceLocation("manhuntmod", "config/quest_progress.json"));
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
        initAbilityButtons();
    }

    private void initAbilityButtons() {
        abilityButtons = new ArrayList<>();
        int buttonWidth = 30;
        int buttonHeight = 30;

        // Manually define positions for each ability button
        int[][] buttonPositions = {
                {155, 55}, // Stage 1
                {275, 55}, {315, 55}, // Stage 2
                {155, 170}, // Stage 3
                {295, 170} // Stage 4
        };

        int positionIndex = 0;

        for (Stage stage : questProgress.getStages()) {
            List<QuestProgress.Ability> abilities = stage.getAbilities();
            for (QuestProgress.Ability ability : abilities) {
                if (positionIndex >= buttonPositions.length) break;

                int[] position = buttonPositions[positionIndex];
                int buttonX = position[0];
                int buttonY = position[1];

                AbilityButton abilityButton = new AbilityButton(buttonX, buttonY, buttonWidth, buttonHeight,
                        List.of(ability));

                // Disable the button if the stage is not completed
                abilityButton.active = stage.isCompleted();

                // Set texture based on ability name
                String abilityName = ability.getName().toLowerCase().replace(" ", "_");
                ResourceLocation abilityTexture = new ResourceLocation(ManhuntMod.MOD_ID, "textures/gui/abilities/" + abilityName + ".png");
                abilityButton.setTexture(abilityTexture);

                abilityButtons.add(abilityButton);

                positionIndex++;
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);

        Minecraft.getInstance().getTextureManager().bindForSetup(BACKGROUND_TEXTURE);
        int x = (this.width - BACKGROUND_WIDTH) / 2;
        int y = (this.height - BACKGROUND_HEIGHT) / 2;
        pGuiGraphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH,
                BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // Manually define positions for each stage name
        int[][] stagePositions = {
                {170, 35},
                {310, 35},
                {170, 150},
                {310, 150}
        };

        for (int i = 0; i < questProgress.getStages().size(); i++) {
            if (i >= stagePositions.length) break;

            Stage stage = questProgress.getStages().get(i);
            int[] stagePosition = stagePositions[i];
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, stage.getName(),
                    stagePosition[0], stagePosition[1], 0xFFFFFF);
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
