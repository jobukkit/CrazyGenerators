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

public class PacketGiveQuestion implements Packet {
    private final DimensionType dimension;
    private final BlockPos pos;
    public PacketGiveQuestion(PacketBuffer buf) {
        dimension = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
    }
    public PacketGiveQuestion(DimensionType type, BlockPos pos) {
        dimension = type;
        this.pos = pos;

    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dimension.getId());
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerWorld world = context.get().getSender().world.getServer().getWorld(dimension);
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof QuestionGeneratorTileEntity) {
                QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
                qgte.setQuestionLevel(qgte.getQuestionLevel()+1);
            }
        });
        context.get().setPacketHandled(true);
    }
}
