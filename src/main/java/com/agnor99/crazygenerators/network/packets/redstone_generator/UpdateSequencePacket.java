package com.agnor99.crazygenerators.network.packets.redstone_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSequencePacket implements ClientPacket {
    BlockPos pos;

    public UpdateSequencePacket(BlockPos pos){
        this.pos = pos;
    }

    public UpdateSequencePacket(PacketBuffer buf){
        pos = buf.readBlockPos();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().getServerWorld();
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof RedstoneGeneratorTileEntity) {
            RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) te;
            rgte.generateTarget();
        }
    }
}
