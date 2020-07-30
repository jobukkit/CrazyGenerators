package com.agnor99.crazygenerators.network.packets.sync;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPongSyncResponse extends PacketAbstractSyncResponse {

    public PacketPongSyncResponse(int energy, boolean shouldClose) {
        super(energy, shouldClose);
    }

    public PacketPongSyncResponse(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);

        context.get().setPacketHandled(true);
    }
}
