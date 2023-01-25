package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.function.Supplier;

public class PacketQuestionSyncResponse extends PacketAbstractSyncResponse {
    String question;
    String[] answers = new String[4];
    public PacketQuestionSyncResponse(@NotNull Question question, int energy) {
        super(energy);
        this.question = question.getQuestion();
        answers = question.getAnswerPossibilities();
    }

    public PacketQuestionSyncResponse(PacketBuffer buf) {
        super(buf);
        question = buf.readString();
        for(int i = 0; i < 4; i++) {
            answers[i] = buf.readString();
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeString(question);
        for(String string: answers) {
            buf.writeString(string);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);

        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof QuestionGeneratorScreen) {
            ((QuestionGeneratorScreen) screen).updateQuestion(question, answers);
        }
    }
}
