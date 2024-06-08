package com.sruproductions.manhuntmod.data;

import java.util.ArrayList;
import java.util.List;

public class QuestProgress {

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

    private List<Stage> stages;

    public QuestProgress() {
        this.stages = new ArrayList<>();
    }

    public List<Stage> getStages() {
        return stages;
    }

//    public void loadDefaultStagesFromResource(ResourceLocation resourceLocation) throws IOException {
//        InputStream inputStream = Minecraft.getInstance().getResourceManager().open(resourceLocation);
//        try (Reader reader = new InputStreamReader(inputStream)) {
//            QuestProgress loadedProgress = GSON.fromJson(reader, QuestProgress.class);
//            this.stages = loadedProgress.stages;
//        }
//    }
}
