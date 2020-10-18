package com.agnor99.crazygenerators.network.packets.position_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WayPointChatMessagePacket extends ClientPacket {
    String chatMessage;
    public WayPointChatMessagePacket(String chatMessage) {
        super((BlockPos)null);
        this.chatMessage = chatMessage;
    }
    public WayPointChatMessagePacket(PacketBuffer buf) {
        super(buf);
        chatMessage = buf.readString(Short.MAX_VALUE);
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeString(chatMessage);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return true;
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        context.get().getSender().sendMessage(new StringTextComponent(chatMessage), context.get().getSender().getGameProfile().getId());
    }
}
