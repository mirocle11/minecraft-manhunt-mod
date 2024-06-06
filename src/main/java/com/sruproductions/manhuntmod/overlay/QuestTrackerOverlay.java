package com.sruproductions.manhuntmod.overlay;

import com.sruproductions.manhuntmod.ModResources;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.data.QuestProgress.Quest;
import com.sruproductions.manhuntmod.data.QuestProgress.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = "manhuntmod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuestTrackerOverlay {

    private static final QuestProgress questProgress = new QuestProgress();
    private static boolean visible = true;

    public static void init(final FMLClientSetupEvent event) {
        loadQuestProgress();
        MinecraftForge.EVENT_BUS.register(new QuestTrackerOverlay());
    }

    private static void loadQuestProgress() {
        try {
            questProgress.loadFromFile(ModResources.QUEST_PROGRESS_JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            GuiGraphics guiGraphics = event.getGuiGraphics();
            Stage currentStage = questProgress.getCurrentStage();
            if (currentStage == null) {
                return; // All stages are completed
            }

            int x = 10;
            int y = 10;

            // Render the current stage name
            drawString(guiGraphics, currentStage.getName(), x, y, 0xFFFFFF);
            y += 12;

            // Render the quests
            for (Quest quest : currentStage.getQuests()) {
                int color = quest.isCompleted() ? 0x00FF00 : 0xFF0000; // Green if completed, red otherwise
                drawString(guiGraphics, quest.getName(), x, y, color);
                y += 12;
            }
        }
    }

    public static void toggleVisibility() {
        visible = !visible;
    }

    private static void drawString(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        guiGraphics.drawString(Minecraft.getInstance().font, text, x, y, color);
    }

    // Refresh the overlay data when the quest is completed
    public static void refreshOverlay() {
        loadQuestProgress();
    }
}
