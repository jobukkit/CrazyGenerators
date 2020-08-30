package com.agnor99.crazygenerators.network.packets.position_generator;

import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NewCoordsPacket implements Packet {

    BlockPos pos;
    DimensionType type;
    public NewCoordsPacket(DimensionType type, BlockPos pos) {
        this.pos = pos;
        this.type = type;
    }
    public NewCoordsPacket(PacketBuffer buf) {
        pos = buf.readBlockPos();
        type = DimensionType.getById(buf.readInt());
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(type.getId());
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(type);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof PositionGeneratorTileEntity) {
            PositionGeneratorTileEntity pgte = (PositionGeneratorTileEntity) te;
            pgte.updateFlag();
        }
        context.get().setPacketHandled(true);
    }
}
