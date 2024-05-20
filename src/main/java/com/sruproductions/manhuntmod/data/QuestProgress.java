package com.sruproductions.manhuntmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestProgress {

    public static class Quest {
        private String name;
        private boolean completed;

        public Quest(String name, boolean completed) {
            this.name = name;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    public static class Ability {
        private String name;
        private String description;

        public Ability(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Stage {
        private String name;
        private Ability ability;
        private List<Quest> quests;
        private boolean completed;

        public Stage(String name, Ability ability, List<Quest> quests, boolean completed) {
            this.name = name;
            this.ability = ability;
            this.quests = quests;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public Ability getAbility() {
            return ability;
        }

        public List<Quest> getQuests() {
            return quests;
        }

        public boolean isCompleted() {
            return completed;
        }
    }

    public Stage getCurrentStage() {
        for (Stage stage : stages) {
            if (!stage.isCompleted()) {
                return stage;
            }
        }
        return null; // All stages are completed
    }


    private List<Stage> stages;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public QuestProgress() {
        this.stages = new ArrayList<>();
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void saveToFile(File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            GSON.toJson(this, writer);
        }
    }

    public void loadFromFile(File file, ResourceLocation defaultConfigResource) throws IOException {
        if (!file.exists()) {
            loadDefaultStagesFromResource(defaultConfigResource);
            saveToFile(file); // Save the default configuration to the file
        } else {
            try (Reader reader = new FileReader(file)) {
                QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
                this.stages = loadedProgress.stages;
            }
        }
    }

    public void loadDefaultStagesFromResource(ResourceLocation resourceLocation) throws IOException {
        InputStream inputStream = Minecraft.getInstance().getResourceManager().open(resourceLocation);
        try (Reader reader = new InputStreamReader(inputStream)) {
            QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
            this.stages = loadedProgress.stages;
        }
    }
}
