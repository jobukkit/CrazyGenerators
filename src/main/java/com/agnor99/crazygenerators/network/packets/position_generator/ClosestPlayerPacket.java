package com.agnor99.crazygenerators.network.packets.position_generator;

import com.agnor99.crazygenerators.client.gui.PositionGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClosestPlayerPacket implements ServerPacket {
    String playerName;

    public ClosestPlayerPacket(String playerName) {
        this.playerName = playerName;
    }

    public ClosestPlayerPacket(PacketBuffer buf) {
        playerName = buf.readString(32767);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeString(playerName);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof PositionGeneratorScreen) {
            PositionGeneratorScreen positionGeneratorScreen = (PositionGeneratorScreen) screen;
            PositionGeneratorTileEntity te = (PositionGeneratorTileEntity) positionGeneratorScreen.getContainer().getTileEntity();
            te.flag.playerName = playerName;
        }
    }
}
