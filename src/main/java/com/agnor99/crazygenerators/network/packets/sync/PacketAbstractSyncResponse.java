package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.objects.container.GeneratorContainer;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketAbstractSyncResponse implements ServerPacket {
    int energy;
    public PacketAbstractSyncResponse(int energy) {
        this.energy = energy;
    }
    public PacketAbstractSyncResponse(PacketBuffer buf) {
        energy = buf.readInt();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(energy);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {

        GeneratorTileEntity te = getTileEntity();

        te.setEnergy(energy);
        context.get().setPacketHandled(true);
    }
    protected static GeneratorTileEntity getTileEntity() {
        GeneratorScreen screen = (GeneratorScreen)Minecraft.getInstance().currentScreen;

        GeneratorContainer container = (GeneratorContainer)screen.getContainer();
        return container.getTileEntity();
    }
}
