package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.GeneratorScreen;
import com.agnor99.crazygenerators.container.GeneratorContainer;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketAbstractSyncResponse implements ServerPacket {
    int energy;
    boolean shouldClose;
    public PacketAbstractSyncResponse(int energy, boolean shouldClose) {
        this.energy = energy;
        this.shouldClose = shouldClose;
    }
    public PacketAbstractSyncResponse(PacketBuffer buf) {
        energy = buf.readInt();
        shouldClose = buf.readBoolean();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(energy);
        buf.writeBoolean(shouldClose);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {

        if(shouldClose) {
            Minecraft.getInstance().player.closeScreen();
            return;
        }
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
