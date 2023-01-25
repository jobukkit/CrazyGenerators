package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAnswerResponse implements ServerPacket {
    private final BlockPos pos;
    private final String question;
    private final String[] answers;
    public PacketAnswerResponse(PacketBuffer buf) {
        pos = buf.readBlockPos();
        question = buf.readString();
        answers = new String[4];
        for(int i = 0; i < 4; i++) {
            answers[i] = buf.readString();
        }
    }
    public PacketAnswerResponse(BlockPos pos, Question question) {
        this.pos = pos;
        this.question = question.getQuestion();
        answers = question.getAnswerPossibilities();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeString(question);
        for(int i = 0; i < 4; i++) {
            buf.writeString(answers[i]);
        }
    }


    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof QuestionGeneratorScreen) {
            QuestionGeneratorScreen qgScreen = (QuestionGeneratorScreen) screen;
            qgScreen.updateQuestion(question, answers);
        }
        context.get().setPacketHandled(true);
    }
}
