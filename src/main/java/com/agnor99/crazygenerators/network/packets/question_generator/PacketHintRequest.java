package com.agnor99.crazygenerators.network.packets.question_generator;

import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
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

public class PacketHintRequest implements Packet {

    BlockPos pos;
    DimensionType type;
    public PacketHintRequest(DimensionType type, BlockPos pos) {
        this.pos = pos;
        this.type = type;
    }

    public PacketHintRequest(PacketBuffer buf) {
        pos = buf.readBlockPos();
        type = DimensionType.getById(buf.readInt());
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(type.getId());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        ServerWorld world = context.get().getSender().world.getServer().getWorld(type);
        TileEntity te = world.getTileEntity(pos);
        ServerPlayerEntity player = context.get().getSender();
        if(te instanceof QuestionGeneratorTileEntity) {
            QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) te;
            if(qgte.getTipsAvailable()>1) {
                qgte.setTipsAvailable(qgte.getTipsAvailable() - 1);
                String[] wrongAnswers = qgte.getQuestion().getWrongAnswers();
                PacketHint hintResponse = new PacketHint(pos, wrongAnswers[0], wrongAnswers[1]);
                NetworkUtil.INSTANCE.sendTo(hintResponse, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
        context.get().setPacketHandled(true);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }
}
