package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAnswerResponse implements Packet {
    private final BlockPos pos;
    private final boolean wasAnswerCorrect;
    private final String question;
    private final String[] answers;
    public PacketAnswerResponse(PacketBuffer buf) {
        pos = buf.readBlockPos();
        question = buf.readString();
        answers = new String[4];
        for(int i = 0; i < 4; i++) {
            answers[i] = buf.readString();
        }
        wasAnswerCorrect = buf.readBoolean();
    }
    public PacketAnswerResponse(BlockPos pos, Question question, boolean wasAnswerCorrect) {
        this.pos = pos;
        this.question = question.getQuestion();
        answers = question.getAnswerPossibilities();
        this.wasAnswerCorrect = wasAnswerCorrect;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeString(question);
        for(int i = 0; i < 4; i++) {
            buf.writeString(answers[i]);
        }
        buf.writeBoolean(wasAnswerCorrect);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof QuestionGeneratorTileEntity) {
                QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
                qgte.updateQuestion(question, answers);
            }
        });
        context.get().setPacketHandled(true);
    }
}
