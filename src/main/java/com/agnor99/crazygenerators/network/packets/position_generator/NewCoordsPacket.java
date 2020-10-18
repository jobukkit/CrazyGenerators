package com.agnor99.crazygenerators.network.packets.position_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NewCoordsPacket extends ClientPacket {

    public NewCoordsPacket(BlockPos pos) {
        super(pos);
    }
    public NewCoordsPacket(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        TileEntity te = getTileEntity(context);
        if(te instanceof PositionGeneratorTileEntity) {
            PositionGeneratorTileEntity pgte = (PositionGeneratorTileEntity) te;
            pgte.updateFlag();
        }
        context.get().setPacketHandled(true);
    }
}
