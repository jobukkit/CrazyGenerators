package com.agnor99.crazygenerators.network.packets.sync;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.ItemGeneratorTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketItemSyncResponse extends PacketAbstractSyncResponse {
    Item item;
    public PacketItemSyncResponse(Item item, int energy) {
        super(energy);
        this.item = item;
    }

    public PacketItemSyncResponse(PacketBuffer buf) {
        super(buf);
        item = buf.readItemStack().getItem();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeItemStack(new ItemStack(item));
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        super.doWork(context);
        GeneratorTileEntity gte = getTileEntity();
        if(gte instanceof ItemGeneratorTileEntity) {
            ItemGeneratorTileEntity igte = (ItemGeneratorTileEntity) gte;
            igte.toFind = item;
        }
    }
}
