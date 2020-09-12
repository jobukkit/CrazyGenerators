package com.agnor99.crazygenerators.network.packets.redstone_generator;

import com.agnor99.crazygenerators.client.gui.RedstoneGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SequencePacket implements ServerPacket {
    boolean[] redstoneData;

    public SequencePacket(boolean[] redstoneData) {
        this.redstoneData = redstoneData;
        if(redstoneData.length != 20) {
            throw new IllegalArgumentException("Too many Datapoins");
        }
    }

    public SequencePacket(PacketBuffer buf) {
        redstoneData = new boolean[20];
        for(int i = 0; i < 20; i++) {
            redstoneData[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        for(int i = 0; i < 20; i++) {
            buf.writeBoolean(redstoneData[i]);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        GeneratorTileEntity gte  = getTileEntity();
        if(gte instanceof RedstoneGeneratorTileEntity) {
            RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) gte;
            rgte.targetRedstoneData = redstoneData;
        }
    }
}
