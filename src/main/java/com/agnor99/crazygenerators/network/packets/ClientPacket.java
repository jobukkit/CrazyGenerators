package com.agnor99.crazygenerators.network.packets;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface ClientPacket extends Packet {
    default GeneratorTileEntity getTileEntity(BlockPos pos, Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(context.get().getSender().dimension);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof GeneratorTileEntity) {
            return (GeneratorTileEntity) te;
        }
        return null;
    }
}
