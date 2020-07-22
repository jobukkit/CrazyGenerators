package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
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

        ClientWorld world = Minecraft.getInstance().world;
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof QuestionGeneratorTileEntity) {
            QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
            if(fake1.equals(qgte.displayAnswer0)) {
                qgte.displayAnswer0 = "";
            }
            if(fake1.equals(qgte.displayAnswer1)) {
                qgte.displayAnswer1 = "";
            }
            if(fake1.equals(qgte.displayAnswer2)) {
                qgte.displayAnswer2 = "";
            }
            if(fake1.equals(qgte.displayAnswer3)) {
                qgte.displayAnswer3 = "";
            }
            if(fake2.equals(qgte.displayAnswer0)) {
                qgte.displayAnswer0 = "";
            }
            if(fake2.equals(qgte.displayAnswer1)) {
                qgte.displayAnswer1 = "";
            }
            if(fake2.equals(qgte.displayAnswer2)) {
                qgte.displayAnswer2 = "";
            }
            if(fake2.equals(qgte.displayAnswer3)) {
                qgte.displayAnswer3 = "";
            }
        }
        context.get().setPacketHandled(true);
    }
}
