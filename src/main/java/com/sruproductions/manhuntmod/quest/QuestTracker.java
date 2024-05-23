package com.sruproductions.manhuntmod.quest;

import com.sruproductions.manhuntmod.data.QuestProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = "manhuntmod", bus = Bus.MOD, value = Dist.CLIENT)
public class QuestTracker {

    private final QuestProgress questProgress;
    private final File saveFile;

    public QuestTracker() {
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

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new QuestTracker());
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            if (event.getEntity() instanceof Cow) {
                completeQuest(0, 0); // Complete stage 1, quest 1
            }
        }
    }

    private void completeQuest(int stageIndex, int questIndex) {
        QuestProgress.Stage stage = questProgress.getStages().get(stageIndex);
        QuestProgress.Quest quest = stage.getQuests().get(questIndex);
        if (!quest.isCompleted()) {
            quest.setCompleted(true);
            saveQuestProgress();
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Quest Completed: " + quest.getName()));
        }
    }
}
