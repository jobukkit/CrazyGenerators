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
    boolean[] targetRedstoneData;
    public PacketRedstoneSyncResponse(int energy, boolean[] targetRedstoneData) {
        super(energy);
        this.targetRedstoneData = targetRedstoneData == null ? new boolean[20] : targetRedstoneData;
    }

    public PacketRedstoneSyncResponse(PacketBuffer buf) {
        super(buf);
        targetRedstoneData = new boolean[20];
        for(int i = 0; i < 20; i++) {
            targetRedstoneData[i] = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        for(int i = 0; i < 20; i++) {
            buf.writeBoolean(targetRedstoneData[i]);
        }
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        GeneratorTileEntity gte = getTileEntity();
        if(gte instanceof RedstoneGeneratorTileEntity) {
            RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) gte;
            rgte.targetRedstoneData = targetRedstoneData;
        }
    }
}
