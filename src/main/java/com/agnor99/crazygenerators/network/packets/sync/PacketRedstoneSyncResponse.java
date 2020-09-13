package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.function.Supplier;

public class PacketRedstoneSyncResponse extends PacketAbstractSyncResponse {
    int targetRedstoneData0,targetRedstoneData1;
    public PacketRedstoneSyncResponse(int energy, int targetRedstoneData0, int targetRedstoneData1) {
        super(energy);
        this.targetRedstoneData0 = targetRedstoneData0;
        this.targetRedstoneData1 = targetRedstoneData1;
    }

    public PacketRedstoneSyncResponse(PacketBuffer buf) {
        super(buf);
        targetRedstoneData0 = buf.readInt();
        targetRedstoneData1 = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeInt(targetRedstoneData0);
        buf.writeInt(targetRedstoneData1);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        GeneratorTileEntity gte = getTileEntity();
        if(gte instanceof RedstoneGeneratorTileEntity) {
            RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) gte;
            rgte.targetRedstoneData0 = this.targetRedstoneData0;
            rgte.targetRedstoneData1 = this.targetRedstoneData1;
        }
    }
}
