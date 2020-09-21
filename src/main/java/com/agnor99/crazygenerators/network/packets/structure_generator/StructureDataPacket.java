package com.agnor99.crazygenerators.network.packets.structure_generator;

import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureDataPacket implements ServerPacket {

    int[][] structureData = new int[5][5];

    public StructureDataPacket(int[][] structureData) {
        this.structureData = structureData;
    }

    public StructureDataPacket(PacketBuffer buf) {
        for(int x = 0; x < 5; x++) {
            structureData[x] = buf.readVarIntArray();
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        for(int x = 0; x < 5; x++) {
            buf.writeVarIntArray(structureData[x]);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        StructureGeneratorTileEntity gte = (StructureGeneratorTileEntity)getTileEntity();
        gte.structureTarget = structureData;
    }
}
