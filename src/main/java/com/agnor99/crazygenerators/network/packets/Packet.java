package com.agnor99.crazygenerators.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet {
    void toBytes(PacketBuffer buf);
    void handle(Supplier<NetworkEvent.Context> context);
}
