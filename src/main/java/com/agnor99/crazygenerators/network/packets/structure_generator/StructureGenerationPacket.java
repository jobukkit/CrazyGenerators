package com.agnor99.crazygenerators.network.packets.structure_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureGenerationPacket implements ClientPacket {
    BlockPos pos;
    public StructureGenerationPacket(BlockPos pos) {
        this.pos = pos;
    }

    public StructureGenerationPacket(PacketBuffer buf) {
        pos = buf.readBlockPos();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        StructureGeneratorTileEntity gte = (StructureGeneratorTileEntity) getTileEntity(pos, context);
        gte.updateMap();
    }
}
