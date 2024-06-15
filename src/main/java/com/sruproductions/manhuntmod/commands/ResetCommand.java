package com.sruproductions.manhuntmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sruproductions.manhuntmod.data.QuestProgress;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class ResetCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resetprogress")
                .then(Commands.argument("stage", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            int stage = IntegerArgumentType.getInteger(context, "stage");
                            return resetProgress(context.getSource(), stage);
                        }))
        );
    }

    private static int resetProgress(CommandSourceStack source, int stage) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        // Assuming player data is stored in player capabilities or similar
        QuestProgress progress = QuestProgress.getInstance();
        if (stage < 0 || stage >= progress.getStages().size()) {
            source.sendFailure(Component.literal("Invalid stage number."));
            return 0;
        }

        progress.resetProgressToStage(stage);
        source.sendSuccess((Supplier<Component>) Component.literal("Progress reset to stage " + (stage + 1)), true);
        return 1;
    }
}
