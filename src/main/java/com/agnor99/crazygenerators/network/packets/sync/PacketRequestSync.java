package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketAnswerResponse;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRequestSync implements Packet {

    private final DimensionType dimension;
    private final BlockPos pos;

    public PacketRequestSync(PacketBuffer buf) {
        dimension = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
    }
    public PacketRequestSync(DimensionType type, BlockPos pos) {
        dimension = type;
        this.pos = pos;

    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dimension.getId());
        buf.writeBlockPos(pos);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {

        ServerWorld world = context.get().getSender().world.getServer().getWorld(dimension);
        TileEntity te = world.getTileEntity(pos);
        ServerPlayerEntity player = context.get().getSender();
        if(te instanceof GeneratorTileEntity) {
            GeneratorTileEntity gte = (GeneratorTileEntity) te;
            gte.players.add(player);
            PacketAbstractSyncResponse syncResponse = gte.generateSyncPacket();
            NetworkUtil.INSTANCE.sendTo(syncResponse, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }

        context.get().setPacketHandled(true);
    }

    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPos(pos, context.get().getSender().getServerWorld());
    }
}
