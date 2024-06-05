package com.sruproductions.manhuntmod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CommandPacket {
    private final String command;

    public CommandPacket(String command) {
        this.command = command;
    }

    public static void encode(CommandPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.command);
    }

    public static CommandPacket decode(FriendlyByteBuf buffer) {
        return new CommandPacket(buffer.readUtf(32767));
    }

    public static void handle(CommandPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                context.getSender().server.getCommands().performPrefixedCommand(context.getSender().createCommandSourceStack(), msg.command);
            }
        });
        context.setPacketHandled(true);
    }
}
