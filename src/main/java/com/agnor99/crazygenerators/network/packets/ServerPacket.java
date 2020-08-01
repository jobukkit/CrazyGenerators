package com.agnor99.crazygenerators.network.packets;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface ServerPacket extends Packet {

    @Override
    default boolean isValid(Supplier<NetworkEvent.Context> context) {
        return true;
    }
}
