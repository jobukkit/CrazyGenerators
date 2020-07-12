package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAnswer implements Packet {
    private final DimensionType dimension;
    private final BlockPos pos;
    private final int answer;
    public PacketAnswer(PacketBuffer buf) {
        dimension = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
        answer = buf.readInt();
    }
    public PacketAnswer(DimensionType type, BlockPos pos, int answer) {
        dimension = type;
        this.pos = pos;
        this.answer = answer;

    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dimension.getId());
        buf.writeBlockPos(pos);
        buf.writeInt(answer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerWorld world = context.get().getSender().world.getServer().getWorld(dimension);
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof QuestionGeneratorTileEntity) {
                QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
                qgte.validateAnswer(answer);
            }
        });
        context.get().setPacketHandled(true);
    }
}
