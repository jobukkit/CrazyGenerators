package com.agnor99.crazygenerators.network.packets;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class ClientPacket implements Packet {
    protected BlockPos pos;

    protected ClientPacket(BlockPos pos) {
        this.pos = pos;
    }

    protected ClientPacket(PacketBuffer buf) {
        pos = buf.readBlockPos();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    protected GeneratorTileEntity getTileEntity(Supplier<NetworkEvent.Context> context) {

        TileEntity te = context.get().getSender().world.getTileEntity(pos);
        if(te instanceof GeneratorTileEntity) {
            return (GeneratorTileEntity) te;
        }
        return null;
    }

    public boolean checkBlockPosWithPlayer(ServerPlayerEntity player) {
        return checkBlockPosWithPlayer(pos, player);
    }
}
