package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.function.Supplier;

public class PacketStructureSyncResponse extends PacketAbstractSyncResponse {

    public PacketStructureSyncResponse(int energy) {
        super(energy);
    }

    public PacketStructureSyncResponse(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
    }
}
