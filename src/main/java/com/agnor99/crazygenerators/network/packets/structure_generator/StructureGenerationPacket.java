package com.agnor99.crazygenerators.network.packets.structure_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureGenerationPacket extends ClientPacket {
    public StructureGenerationPacket(BlockPos pos) {
        super(pos);
    }

    public StructureGenerationPacket(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        StructureGeneratorTileEntity gte = (StructureGeneratorTileEntity) getTileEntity(context);
        gte.updateMap();
    }
}
