package com.agnor99.crazygenerators.network.packets.timing_generator;

import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketButtonPress implements Packet {
    private final DimensionType dimension;
    private final BlockPos pos;
    private final int tickClicked;
    public PacketButtonPress(PacketBuffer buf) {
        dimension = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
        tickClicked = buf.readInt();
    }
    public PacketButtonPress(DimensionType type, BlockPos pos, int tickClicked) {
        dimension = type;
        this.pos = pos;
        this.tickClicked = tickClicked;

    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dimension.getId());
        buf.writeBlockPos(pos);
        buf.writeInt(tickClicked);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(dimension);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TimingGeneratorTileEntity) {
            TimingGeneratorTileEntity tgte = (TimingGeneratorTileEntity) te;
            int energyAdded = tgte.addClickEnergy(context.get().getSender().ping);
            int delay = tgte.calcDelay(context.get().getSender().ping);
            tgte.generateUnlockData();

            PacketButtonPressResponse response = new PacketButtonPressResponse(
              delay,
              energyAdded
            );
            tgte.sendToAllLooking(response);
        }
        context.get().setPacketHandled(true);
    }
}
