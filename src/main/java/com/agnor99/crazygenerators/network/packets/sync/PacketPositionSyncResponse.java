package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.PositionGeneratorScreen;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPositionSyncResponse extends PacketAbstractSyncResponse {
    String playerName;
    public PacketPositionSyncResponse(String playerName, int energy) {
        super(energy);
        this.playerName = playerName;
    }

    public PacketPositionSyncResponse(PacketBuffer buf) {
        super(buf);
        playerName = buf.readString(32767);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeString(playerName);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof PositionGeneratorScreen) {
            ((PositionGeneratorTileEntity)(getTileEntity())).flag.playerName = playerName;
        }
    }
}
