package com.agnor99.crazygenerators.network.packets.timing_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketButtonPress extends ClientPacket {
    private final int tickClicked;
    public PacketButtonPress(PacketBuffer buf) {
        super(buf);
        tickClicked = buf.readInt();
    }
    public PacketButtonPress(BlockPos pos, int tickClicked) {
        super(pos);
        this.tickClicked = tickClicked;

    }
    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeInt(tickClicked);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        TileEntity te = getTileEntity(context);
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
