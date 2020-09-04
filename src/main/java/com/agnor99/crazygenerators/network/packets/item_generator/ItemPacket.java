package com.agnor99.crazygenerators.network.packets.item_generator;

import com.agnor99.crazygenerators.network.packets.ServerPacket;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.ItemGeneratorTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemPacket implements ServerPacket {
    Item item;

    public ItemPacket(Item item) {
        this.item = item;
    }
    public ItemPacket(PacketBuffer buf) {
        item = buf.readItemStack().getItem();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeItemStack(new ItemStack(item));
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        GeneratorTileEntity gte = getTileEntity(context);
        if(gte instanceof ItemGeneratorTileEntity) {
            ItemGeneratorTileEntity igte = (ItemGeneratorTileEntity) gte;
            igte.toFind = item;
        }
    }
}
