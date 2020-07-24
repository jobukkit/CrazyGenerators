package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.Packet;
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

public class PacketAnswer implements Packet {
    private final DimensionType dimension;
    private final BlockPos pos;
    private final String answer;
    public PacketAnswer(PacketBuffer buf) {
        dimension = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
        answer = buf.readString(32767);
    }
    public PacketAnswer(DimensionType type, BlockPos pos, String answer) {
        dimension = type;
        this.pos = pos;
        this.answer = answer;

    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dimension.getId());
        buf.writeBlockPos(pos);
        buf.writeString(answer);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(dimension);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof QuestionGeneratorTileEntity) {
            QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
            if(qgte.questionGeneratedTime+QuestionGeneratorTileEntity.ANSWER_DELAY > qgte.getTick()) return;

            qgte.handleAnswer(answer);
            ServerPlayerEntity player = context.get().getSender();

            PacketAnswerResponse response = new PacketAnswerResponse(
                    pos,
                    qgte.getQuestion()
            );

            NetworkUtil.INSTANCE.sendTo(response, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
        context.get().setPacketHandled(true);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }
}
