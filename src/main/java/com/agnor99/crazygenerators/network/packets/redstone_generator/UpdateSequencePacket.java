package com.agnor99.crazygenerators.network.packets.redstone_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSequencePacket extends ClientPacket {
    public UpdateSequencePacket(BlockPos pos){
        super(pos);
    }

    public UpdateSequencePacket(PacketBuffer buf){
        super(buf);
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
