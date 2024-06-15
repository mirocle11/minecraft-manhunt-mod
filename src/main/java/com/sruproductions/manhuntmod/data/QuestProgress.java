package com.sruproductions.manhuntmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QuestProgress {

    private static QuestProgress instance;
    public static QuestProgress getInstance() {
        if (instance == null) {
            instance = new QuestProgress();
        }
        return instance;
    }

    private List<Stage> stages;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path GLOBAL_SAVE_FILE = getGlobalSaveFilePath();

    private static Path getGlobalSaveFilePath() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return FMLPaths.CONFIGDIR.get().resolve("manhuntmod").resolve("quest_progress.json");
        }
        return null;
    }

    public static class Stage {
        private String name;
        private List<Ability> abilities;
        private List<Quest> quests;
        private boolean completed;

        public Stage(String name, List<Ability> abilities, List<Quest> quests, boolean completed) {
            this.name = name;
            this.abilities = abilities;
            this.quests = quests;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public List<Ability> getAbilities() {
            return abilities;
        }

        public List<Quest> getQuests() {
            return quests;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void checkCompletion() {
            for (Quest quest : quests) {
                if (!quest.isCompleted()) {
                    return;
                }
            }
            completed = true;
        }

        public void resetQuests() {
            for (Quest quest : quests) {
                quest.setCompleted(false);
            }
            this.completed = false;
        }
    }

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

    public Stage getCurrentStage() {
        for (Stage stage : stages) {
            if (!stage.isCompleted()) {
                return stage;
            }
        }
        return null; // All stages are completed
    }

    public QuestProgress() {
        this.stages = new ArrayList<>();
    }

    public List<Stage> getStages() {
        return stages;
    }

    public boolean isAbilityUnlocked(String abilityName) {
        for (Stage stage : stages) {
            for (Ability ability : stage.getAbilities()) {
                if (ability.getName().toLowerCase().replace(" ", "_").equals(abilityName)) {
                    return stage.isCompleted();
                }
            }
        }
        return false;
    }

    public void resetProgressToStage(int stageIndex) {
        if (stageIndex >= 0 && stageIndex < stages.size()) {
            for (int i = 0; i <= stageIndex; i++) {
                stages.get(i).resetQuests();
            }
            for (int i = stageIndex + 1; i < stages.size(); i++) {
                stages.get(i).resetQuests();
                stages.get(i).completed = false;
            }
        }
    }

    public void saveToFile() throws IOException {
        if (FMLEnvironment.dist == Dist.CLIENT && GLOBAL_SAVE_FILE != null) {
            Files.createDirectories(GLOBAL_SAVE_FILE.getParent());
            try (Writer writer = new FileWriter(GLOBAL_SAVE_FILE.toFile())) {
                GSON.toJson(this, writer);
            }
        }
    }

    public void loadFromFile(ResourceLocation defaultConfigResource) throws IOException {
        if (FMLEnvironment.dist == Dist.CLIENT && GLOBAL_SAVE_FILE != null) {
            if (!Files.exists(GLOBAL_SAVE_FILE)) {
                loadDefaultStagesFromResource(defaultConfigResource);
                saveToFile(); // Save the default configuration to the file
            } else {
                try (Reader reader = new FileReader(GLOBAL_SAVE_FILE.toFile())) {
                    QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
                    this.stages = loadedProgress.stages;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void loadDefaultStagesFromResource(ResourceLocation resourceLocation) throws IOException {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            InputStream inputStream = Minecraft.getInstance().getResourceManager().open(resourceLocation);
            try (Reader reader = new InputStreamReader(inputStream)) {
                QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
                this.stages = loadedProgress.stages;
            }
        }
    }
}
