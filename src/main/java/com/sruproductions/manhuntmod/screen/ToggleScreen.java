package com.sruproductions.manhuntmod.screen;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import com.sruproductions.manhuntmod.screen.components.AbilityButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ManhuntMod.MOD_ID,
            "textures/gui/gui_prototype2.png");

    private boolean isToggled = false;
    private static final int BACKGROUND_WIDTH = 260;
    private static final int BACKGROUND_HEIGHT = 229;
    private Button toggleButton;

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
            questProgress.loadFromFile(saveFile, new ResourceLocation("manhuntmod",
                    "config/quest_progress.json"));
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

//        this.toggleButton = addRenderableWidget(Button.builder(Component.literal("Toggle"), button -> {
//            isToggled = !isToggled;
//            button.setMessage(Component.literal(isToggled ? "Toggle On" : "Toggle Off"));
//        }).pos(x, y).size(buttonWidth, buttonHeight).build());

        // Initialize ability buttons
        initAbilityButtons();
    }

    private void initAbilityButtons() {
        abilityButtons = new ArrayList<>();
        int buttonWidth = 40;
        int buttonHeight = 40;
        int circleDiameter = 60;
        int padding = 20;

        int startX = (width - BACKGROUND_WIDTH) / 2;
        int startY = (height - BACKGROUND_HEIGHT) / 2;

        int[][] circlePositions = {
                { startX + 30, startY + 20 },
                { startX + 90 + circleDiameter + padding, startY + 20 },
                { startX + 30, startY + 55 + circleDiameter + padding },
                { startX + 90 + circleDiameter + padding, startY + 55 + circleDiameter + padding }
        };

        int index = 0;
        for (Stage stage : questProgress.getStages()) {
            if (index >= circlePositions.length) break;

            int[] position = circlePositions[index];
            int buttonX = position[0] + (circleDiameter - buttonWidth) / 2;
            int buttonY = position[1] + (circleDiameter - buttonHeight) / 2;

            AbilityButton abilityButton = new AbilityButton(buttonX, buttonY, buttonWidth, buttonHeight, stage.getAbility());
            abilityButtons.add(abilityButton);
            index++;
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

        for (AbilityButton abilityButton : abilityButtons) {
            abilityButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        for (AbilityButton abilityButton : abilityButtons) {
            if (abilityButton.isHovered(pMouseX, pMouseY)) {
                renderTooltip(pGuiGraphics, pMouseX, pMouseY, abilityButton.getAbility());
            }
        }
    }

    private void renderTooltip(GuiGraphics pGuiGraphics, int mouseX, int mouseY, QuestProgress.Ability ability) {
        List<String> lines = getStrings(ability);

        int tooltipWidth = lines.stream().mapToInt(line -> font.width(line)).max().orElse(0);
        int tooltipHeight = lines.size() * (font.lineHeight + 2);

        int tooltipX = mouseX + 12;
        int tooltipY = mouseY - 12;

        // Adjust tooltip position if it would go off the screen
        if (tooltipX + tooltipWidth > width) {
            tooltipX = mouseX - tooltipWidth - 12;
        }
        if (tooltipY + tooltipHeight > height) {
            tooltipY = mouseY - tooltipHeight - 12;
        }

        pGuiGraphics.fillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + tooltipHeight + 3,
                -1073741824, -1073741824);

        float scale = 0.75f; // Adjust scale factor to make font smaller
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, 1.0F);
        for (int i = 0; i < lines.size(); i++) {
            pGuiGraphics.drawString(font, lines.get(i), (int)((tooltipX / scale)),
                    (int)((tooltipY + i * (font.lineHeight + 2)) / scale), 0xFFFFFF);
        }
        pGuiGraphics.pose().popPose();
    }

    private @NotNull List<String> getStrings(QuestProgress.Ability ability) {
        List<String> lines = new ArrayList<>();
        lines.add("Ability: " + ability.getName());
        lines.add("Description: " + ability.getDescription());

        for (Stage stage : questProgress.getStages()) {
            if (stage.getAbility().getName().equals(ability.getName())) {
                lines.add("Quests:");
                for (Quest quest : stage.getQuests()) {
                    String questStatus = quest.isCompleted() ? "Completed" : "Incomplete";
                    lines.add("- " + quest.getName() + " (" + questStatus + ")");
                }
            }
        }
        return lines;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_G) {
            this.minecraft.setScreen(null); // Close the screen when G is pressed
            saveQuestProgress(); // Save progress when exiting the screen
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
