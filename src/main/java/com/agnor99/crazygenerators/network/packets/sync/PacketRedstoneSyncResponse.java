package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRedstoneSyncResponse extends PacketAbstractSyncResponse {
    int targetRedstoneData0,targetRedstoneData1;
    public PacketRedstoneSyncResponse(int energy, int targetRedstoneData0, int targetRedstoneData1) {
        super(energy);
        this.targetRedstoneData0 = targetRedstoneData0;
        this.targetRedstoneData1 = targetRedstoneData1;
    }

    public PacketRedstoneSyncResponse(PacketBuffer buf) {
        super(buf);
        targetRedstoneData0 = buf.readInt();
        targetRedstoneData1 = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeInt(targetRedstoneData0);
        buf.writeInt(targetRedstoneData1);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        GeneratorTileEntity gte = getTileEntity();
        if(gte instanceof RedstoneGeneratorTileEntity) {
            RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) gte;
            rgte.targetRedstoneData0 = this.targetRedstoneData0;
            rgte.targetRedstoneData1 = this.targetRedstoneData1;
        }
    }
}
