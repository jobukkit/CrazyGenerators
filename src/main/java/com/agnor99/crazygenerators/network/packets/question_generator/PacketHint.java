package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHint implements ServerPacket {

    BlockPos pos;
    String fake1;
    String fake2;

    public PacketHint(BlockPos pos, String fake1, String fake2) {
        this.pos = pos;
        this.fake1 = fake1;
        this.fake2 = fake2;
    }

    public PacketHint(PacketBuffer buf) {
        pos = buf.readBlockPos();
        fake1 = buf.readString();
        fake2 = buf.readString();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeString(fake1);
        buf.writeString(fake2);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof QuestionGeneratorScreen) {
            QuestionGeneratorScreen qgScreen = (QuestionGeneratorScreen) screen;
            for(int i = 0; i < 4; i++) {
                if (fake1.equals(qgScreen.displayAnswers[i]) || fake2.equals(qgScreen.displayAnswers[i])) {
                    qgScreen.displayAnswers[i] = "";
                }
            }
        }
        context.get().setPacketHandled(true);
    }
}
