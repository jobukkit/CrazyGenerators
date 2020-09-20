package com.agnor99.crazygenerators.network.packets.structure_generator;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.network.packets.ClientPacket;
import com.agnor99.crazygenerators.objects.blocks.StructureConnectorBlock;
import com.agnor99.crazygenerators.objects.blocks.StructureGeneratorBlock;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Iterator;
import java.util.function.Supplier;

public class StructureGetPacket implements ClientPacket {
    BlockPos pos;
    int blockId;

    public StructureGetPacket(BlockPos pos, int blockId) {
        this.pos = pos;
        this.blockId = blockId;
    }

    public StructureGetPacket(PacketBuffer buf) {
        pos = buf.readBlockPos();
        blockId = buf.readInt();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(blockId);
    }

    @Override
    public boolean isValid(Supplier<NetworkEvent.Context> context) {
        return checkBlockPosWithPlayer(pos, context.get().getSender());
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        if(blockId >= 1 && blockId <= 3) {
            switch (blockId){
                case StructureGeneratorTileEntity.STRUCTURE_CONNECTOR:
                    context.get().getSender().addItemStackToInventory(new ItemStack(BlockInit.structure_connector));
                    break;
                case StructureGeneratorTileEntity.STRUCTURE_CORE:
                    context.get().getSender().addItemStackToInventory(new ItemStack(BlockInit.structure_core));
                    break;
                case StructureGeneratorTileEntity.STRUCTURE_ORB:
                    context.get().getSender().addItemStackToInventory(new ItemStack(BlockInit.structure_orb));
                    break;

            }
        }
    }
}
