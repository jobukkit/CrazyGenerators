package com.agnor99.crazygenerators.network.packets.sync;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTimingSyncResponse extends PacketAbstractSyncResponse {

    public PacketTimingSyncResponse(int energy) {
        super(energy);
    }

    public PacketTimingSyncResponse(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
    }
}
