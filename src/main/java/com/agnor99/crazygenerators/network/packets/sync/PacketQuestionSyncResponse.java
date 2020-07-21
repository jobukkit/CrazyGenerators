package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.function.Supplier;

public class PacketQuestionSyncResponse extends PacketAbstractSyncResponse {
    String question;
    String[] answers = new String[4];
    boolean isOpened;
    public PacketQuestionSyncResponse(@NotNull Question question, int energy,@NotNull  BlockPos pos, boolean isOpened) {
        super(energy, pos);
        this.question = question.getQuestion();
        answers = question.getAnswerPossibilities();
        this.isOpened = isOpened;
    }

    public PacketQuestionSyncResponse(PacketBuffer buf) {
        super(buf);
        question = buf.readString();
        for(int i = 0; i < 4; i++) {
            answers[i] = buf.readString();
        }
        isOpened = buf.readBoolean();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeString(question);
        for(String string: answers) {
            buf.writeString(string);
        }
        buf.writeBoolean(isOpened);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        super.handle(context);
        context.get().enqueueWork(() -> {

            ClientWorld world = Minecraft.getInstance().world;
            if(!isOpened) {
                Minecraft.getInstance().player.closeScreen();
                return;
            }
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof QuestionGeneratorTileEntity) {
                QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
                qgte.updateQuestion(question, answers);
                qgte.energy.setEnergy(energy);
            }
        });
        context.get().setPacketHandled(true);
    }
}
