package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStructureSyncResponse extends PacketAbstractSyncResponse {
    int[][] structureData = new int[5][5];
    public PacketStructureSyncResponse(int energy, int[][] structureData) {
        super(energy);
        this.structureData = structureData;
    }

    public PacketStructureSyncResponse(PacketBuffer buf) {
        super(buf);
        for(int x = 0; x < 5; x++){
            structureData[x] = buf.readVarIntArray();
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        for(int x = 0; x < 5; x++) {
            buf.writeVarIntArray(structureData[x]);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        StructureGeneratorTileEntity gte = (StructureGeneratorTileEntity)getTileEntity();
        gte.structureTarget = structureData;
    }
}
