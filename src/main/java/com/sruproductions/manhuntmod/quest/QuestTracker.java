package com.sruproductions.manhuntmod.quest;

import com.sruproductions.manhuntmod.ManhuntMod;
import com.sruproductions.manhuntmod.data.QuestProgress;
import com.sruproductions.manhuntmod.ModResources;
import com.sruproductions.manhuntmod.network.NetworkHandler;
import com.sruproductions.manhuntmod.network.packet.CommandPacket;
import com.sruproductions.manhuntmod.overlay.QuestTrackerOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = "manhuntmod", bus = Bus.MOD, value = Dist.CLIENT)
public class QuestTracker {

    private final QuestProgress questProgress;
    private int ghastKillCount = 0;

    public QuestTracker() {
        this.questProgress = new QuestProgress();
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

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new QuestTracker());
    }

    private int getCurrentStageIndex() {
        for (int i = 0; i < questProgress.getStages().size(); i++) {
            if (!questProgress.getStages().get(i).isCompleted()) {
                return i;
            }
        }
        return -1; // All stages are completed
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            int currentStageIndex = getCurrentStageIndex();
            if (currentStageIndex == 0 && event.getEntity() instanceof Cow) {
                completeQuest(0, 0); // Complete stage 1, quest 1 (Kill a cow)
            } else if (currentStageIndex == 2 && event.getEntity() instanceof Guardian) {
                completeQuest(2, 2); // Complete stage 3, quest 3 (Kill 1 guardian)
            } else if (currentStageIndex == 3 && event.getEntity() instanceof Ghast) {
                ghastKillCount++;
                if (ghastKillCount >= 3) {
                    completeQuest(3, 2); // Complete stage 4, quest 3 (Kill 3 Ghasts)
                    ghastKillCount = 0; // Reset counter after completing the quest
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState blockState = event.getState();
        int currentStageIndex = getCurrentStageIndex();

        // Check if the player mined Lapis Lazuli Ore
        if (currentStageIndex == 0 && blockState.is(Blocks.LAPIS_ORE)) {
            completeQuest(0, 1); // Complete stage 1, quest 2 (Mine 1 Lapis Lazuli)
        }
    }

    @SubscribeEvent
    public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        Item smeltedItem = event.getSmelting().getItem();
        int currentStageIndex = getCurrentStageIndex();

        // Check if the player smelted an iron ingot
        if (currentStageIndex == 0 && smeltedItem == Items.IRON_INGOT) {
            completeQuest(0, 2); // Complete stage 1, quest 3 (Smelt 1 iron ingot)
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Item craftedItem = event.getCrafting().getItem();
        int currentStageIndex = getCurrentStageIndex();

        // Check if the player crafted a piston
        if (currentStageIndex == 1 && craftedItem == Items.PISTON) {
            completeQuest(1, 2); // Complete stage 2, quest 3 (Build 1 piston)
        } else if (currentStageIndex == 2 && isBed(craftedItem)) {
            completeQuest(2, 1); // Complete stage 3, quest 2 (Craft a bed and set your spawn)
        } else if (currentStageIndex == 3 &&
                (craftedItem == Items.DIAMOND_HELMET ||
                        craftedItem == Items.DIAMOND_CHESTPLATE ||
                        craftedItem == Items.DIAMOND_LEGGINGS ||
                        craftedItem == Items.DIAMOND_BOOTS)) {
            completeQuest(3, 0); // Complete stage 4, quest 1 (Equip full diamond armor)
        }
    }

    @SubscribeEvent
    public void onPlayerEnterNether(PlayerEvent.PlayerChangedDimensionEvent event) {
        int currentStageIndex = getCurrentStageIndex();
        if (currentStageIndex == 1 && event.getTo() == Level.NETHER) {
            completeQuest(1, 1); // Complete stage 2, quest 2 (Enter the nether)
        }
    }

    @SubscribeEvent
    public void onPlayerShot(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getSource().getEntity();
            Player victim = (Player) event.getEntity();
            if (attacker.getMainHandItem().getItem() == Items.BOW) {
                int currentStageIndex = getCurrentStageIndex();
                if (currentStageIndex == 1) {
                    completeQuest(1, 0); // Complete stage 2, quest 1 (Shoot a hunter with a bow)
                }
            }
        }
    }

    @SubscribeEvent
    public void onEnchantItem(EnchantmentLevelSetEvent event) {
        int currentStageIndex = getCurrentStageIndex();
        Player player = Minecraft.getInstance().player;
        if (currentStageIndex == 2 && player != null && event.getItem().getItem() == Items.DIAMOND_SWORD) {
            if (EnchantmentHelper.getEnchantments(event.getItem()).containsKey(Enchantments.SHARPNESS)) {
                completeQuest(2, 0); // Complete stage 3, quest 1 (Enchant a sword with sharpness 1)
            }
        }
    }

    private void completeQuest(int stageIndex, int questIndex) {
        QuestProgress.Stage stage = questProgress.getStages().get(stageIndex);
        QuestProgress.Quest quest = stage.getQuests().get(questIndex);
        if (!quest.isCompleted()) {
            quest.setCompleted(true);
            stage.checkCompletion(); // Check if the stage is completed

            if (stage.isCompleted()) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("Stage Completed: " + stage.getName()));
                }

                switch (stage.getName()) {
                    case "Stage 1" -> {
                        NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/learnSpell learn sonic_boom"));
                    }
                    case "Stage 2" -> {
                        NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/learnSpell learn sculk_tentacles"));
                        NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/learnSpell learn spider_aspect"));
                    }
                    case "Stage 3" -> {
                        NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/learnSpell learn acid_orb"));
                    }
                    case "Stage 4" -> {
                        NetworkHandler.INSTANCE.sendToServer(new CommandPacket("/learnSpell learn starfall"));
                    }
                }
            }

            saveQuestProgress();
            QuestTrackerOverlay.refreshOverlay(); // Refresh the overlay
        }
    }

    private boolean isBed(Item item) {
        return item == Items.WHITE_BED ||
                item == Items.ORANGE_BED ||
                item == Items.MAGENTA_BED ||
                item == Items.LIGHT_BLUE_BED ||
                item == Items.YELLOW_BED ||
                item == Items.LIME_BED ||
                item == Items.PINK_BED ||
                item == Items.GRAY_BED ||
                item == Items.LIGHT_GRAY_BED ||
                item == Items.CYAN_BED ||
                item == Items.PURPLE_BED ||
                item == Items.BLUE_BED ||
                item == Items.BROWN_BED ||
                item == Items.GREEN_BED ||
                item == Items.RED_BED ||
                item == Items.BLACK_BED;
    }
}
