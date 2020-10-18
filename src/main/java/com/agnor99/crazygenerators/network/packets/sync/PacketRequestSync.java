package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRequestSync extends ClientPacket {

    public PacketRequestSync(BlockPos pos) {
        super(pos);
    }
    public PacketRequestSync(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {

        TileEntity te = getTileEntity(context);
        ServerPlayerEntity player = context.get().getSender();
        if(te instanceof GeneratorTileEntity) {
            GeneratorTileEntity gte = (GeneratorTileEntity) te;
            gte.players.add(player);
            PacketAbstractSyncResponse syncResponse = gte.generateSyncPacket();
            NetworkUtil.INSTANCE.sendTo(syncResponse, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }

        context.get().setPacketHandled(true);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPos(pos, context.get().getSender().getServerWorld());
    }
}
