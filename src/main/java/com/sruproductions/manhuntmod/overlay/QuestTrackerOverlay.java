package com.sruproductions.manhuntmod.overlay;

import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = "manhuntmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuestTrackerOverlay {

    private static final File SAVE_FILE = new File(Minecraft.getInstance().gameDirectory, "config/quest_progress.json");
    private static final QuestProgress questProgress = new QuestProgress();

    public static void init(final FMLClientSetupEvent event) {
        loadQuestProgress();
    }

    private static void loadQuestProgress() {
        try {
            questProgress.loadFromFile(SAVE_FILE, new ResourceLocation("manhuntmod", "config/quest_progress.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Stage currentStage = questProgress.getCurrentStage();
        if (currentStage == null) {
            return; // All stages are completed
        }

        int x = 10;
        int y = 10;

        // Render the current stage name
        drawString(guiGraphics, currentStage.getName(), x, y, 0xFF0000);
        y += 12;

        // Render the quests
        for (Quest quest : currentStage.getQuests()) {
            int color = quest.isCompleted() ? 0x00FF00 : 0xFF0000; // Green if completed, red otherwise
            drawString(guiGraphics, quest.getName(), x, y, color);
            y += 12;
        }
    }

    private static void drawString(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        guiGraphics.drawString(Minecraft.getInstance().font, text, x, y, color);
    }
}
