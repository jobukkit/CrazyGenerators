package com.agnor99.crazygenerators.network.packets.pong_generator;

import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.tile.PongGeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPongKeys implements Packet {
    boolean up,down,space;
    BlockPos pos;
    DimensionType type;
    public PacketPongKeys(boolean up, boolean down,boolean space, BlockPos pos, DimensionType type) {
        this.up = up;
        this.down = down;
        this.space = space;
        this.pos = pos;
        this.type = type;
    }
    public PacketPongKeys(PacketBuffer buf) {
        up = buf.readBoolean();
        down = buf.readBoolean();
        space = buf.readBoolean();
        pos = buf.readBlockPos();
        type = DimensionType.getById(buf.readInt());
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(up);
        buf.writeBoolean(down);
        buf.writeBoolean(space);
        buf.writeBlockPos(pos);
        buf.writeInt(type.getId());
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos,context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(type);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof PongGeneratorTileEntity) {
            PongGeneratorTileEntity pgte = (PongGeneratorTileEntity) te;
            pgte.game.setPlayerMoving(up, down);
            if(space){
                pgte.game.freeBalls();
            }
        }
        context.get().setPacketHandled(true);
    }
}
