package com.sruproductions.manhuntmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sruproductions.manhuntmod.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class QuestProgressManager {

    private static QuestProgressManager instance;
    private QuestProgress questProgress;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private MinecraftServer server;

    private QuestProgressManager() {
        this.questProgress = new QuestProgress();
    }

    public static QuestProgressManager getInstance() {
        if (instance == null) {
            instance = new QuestProgressManager();
        }
        return instance;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    public QuestProgress getQuestProgress() {
        return questProgress;
    }

    public void saveToFile() throws IOException {
        if (server == null) {
            return; // Server is not set, avoid loading
        }

        File worldDir = server.getWorldPath(LevelResource.ROOT).toFile();
        File dataDir = new File(worldDir, "data/manhuntmod");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File questFile = new File(dataDir, "quest_progress.json");
        if (!questFile.exists()) {
            generateDefaultQuestProgress(questFile);
        }

        try (FileWriter writer = new FileWriter(questFile)) {
            GSON.toJson(this.questProgress, writer);
        }
    }

    private void generateDefaultQuestProgress(File questFile) throws IOException {
        ResourceLocation resourceLocation = ModResources.QUEST_PROGRESS_JSON;
        try (InputStreamReader reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(resourceLocation))) {
            QuestProgress defaultProgress = GSON.fromJson(reader, QuestProgress.class);
            this.questProgress = defaultProgress;
            Files.write(Paths.get(questFile.toURI()), GSON.toJson(defaultProgress).getBytes());
        }
    }

    public void loadFromFile() throws IOException {
        if (server == null) {
            return; // Server is not set, avoid loading
        }

        File worldDir = server.getWorldPath(LevelResource.ROOT).toFile();
        File dataDir = new File(worldDir, "data/manhuntmod");
        File questFile = new File(dataDir, "quest_progress.json");

        if (questFile.exists()) {
            try (FileReader reader = new FileReader(questFile)) {
                QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
                this.questProgress = loadedProgress;
            }
        }
    }
}
