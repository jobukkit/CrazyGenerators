package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketAbstractSyncResponse implements Packet {
    int energy;
    BlockPos pos;
    public PacketAbstractSyncResponse(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }
    public PacketAbstractSyncResponse(PacketBuffer buf) {
        energy = buf.readInt();
        pos = buf.readBlockPos();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {

        context.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof GeneratorTileEntity) {
                GeneratorTileEntity gte = (GeneratorTileEntity) te;
                gte.setEnergy(energy);
            }
        });
        context.get().setPacketHandled(true);
    }
}
