package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAnswer extends ClientPacket {
    private final String answer;
    public PacketAnswer(PacketBuffer buf) {
        super(buf);
        answer = buf.readString(32767);
    }
    public PacketAnswer(BlockPos pos, String answer) {
        super(pos);
        this.answer = answer;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeString(answer);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        TileEntity te = getTileEntity(context);
        if(te instanceof QuestionGeneratorTileEntity) {
            QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
            if(qgte.questionGeneratedTime+QuestionGeneratorTileEntity.ANSWER_DELAY > qgte.getTick()) return;

            qgte.handleAnswer(answer);
            ServerPlayerEntity player = context.get().getSender();

            PacketAnswerResponse response = new PacketAnswerResponse(
                    pos,
                    qgte.getQuestion()
            );
            qgte.sendToAllLooking(response);
        }
        context.get().setPacketHandled(true);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }
}
