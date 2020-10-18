package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHintRequest extends ClientPacket {

    public PacketHintRequest(BlockPos pos) {
        super(pos);
    }

    public PacketHintRequest(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        GeneratorTileEntity te = getTileEntity(context);
        if(te instanceof QuestionGeneratorTileEntity) {
            QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
            if(qgte.getTipsAvailable()>1) {
                qgte.setTipsAvailable(qgte.getTipsAvailable() - 1);
                String[] wrongAnswers = qgte.getQuestion().getWrongAnswers();
                PacketHint hintResponse = new PacketHint(pos, wrongAnswers[0], wrongAnswers[1]);
                qgte.sendToAllLooking(hintResponse);
            }
        }
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }
}
