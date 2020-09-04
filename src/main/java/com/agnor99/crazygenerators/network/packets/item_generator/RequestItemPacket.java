package com.agnor99.crazygenerators.network.packets.item_generator;

import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.ItemGeneratorTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestItemPacket implements ClientPacket {

    BlockPos pos;
    DimensionType type;

    public RequestItemPacket(BlockPos pos, DimensionType type) {
        this.pos = pos;
        this.type = type;
    }

    public RequestItemPacket(PacketBuffer buf) {
        pos = buf.readBlockPos();
        type = DimensionType.getById(buf.readInt());
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(type.getId());
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        GeneratorTileEntity gte = getTileEntity(type, pos, context);
        if(gte instanceof ItemGeneratorTileEntity) {
            ItemGeneratorTileEntity igte = (ItemGeneratorTileEntity) gte;
            igte.generateItem();
        }
    }
}
