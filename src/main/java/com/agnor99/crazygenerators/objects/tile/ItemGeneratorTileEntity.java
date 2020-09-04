package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.network.packets.item_generator.ItemPacket;
import com.agnor99.crazygenerators.objects.container.ItemGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketItemSyncResponse;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class ItemGeneratorTileEntity extends GeneratorTileEntity{

    public Item toFind;
    public static List<Item> allItems;
    public ItemGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }
    public ItemGeneratorTileEntity() {
        this(TileInit.ITEM_GENERATOR.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.item_generator");
    }

    @Override
    public void tick() {
        super.tick();
        if(!shouldTickIntern()) return;

        if(doesItemContainInPlayerInventory()) {
            addEnergy(25000);
            toFind = null;
            sendToAllLooking(new ItemPacket(toFind));
        }

    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ItemGeneratorContainer(id, player, this);
    }

    public void generateItem() {
        if(allItems == null || allItems.size() == 0) {
             allItems = getAllItems();
        }
        List<Item> useableItems = new ArrayList<>();
        useableItems.addAll(allItems);
        if(useableItems.size() == 0) {
            return;
        }
        toFind = useableItems.get(new Random().nextInt(useableItems.size()));
        sendToAllLooking(new ItemPacket(toFind));
    }

    private boolean doesItemContainInPlayerInventory() {
        for(ServerPlayerEntity p: players) {
            for(ItemStack itemStack: p.inventory.armorInventory) {
                if(toFind!=null && toFind.equals(itemStack.getItem())) {
                    return true;
                }
            }
            for(ItemStack itemStack: p.inventory.mainInventory) {
                if(toFind!=null && toFind.equals(itemStack.getItem())) {
                    return true;
                }
            }
            for(ItemStack itemStack: p.inventory.offHandInventory) {
                if(toFind!=null && toFind.equals(itemStack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
    private static List<Item> getAllItems() {
        Iterator<Item> itemIterator = Registry.ITEM.iterator();
        List<Item> itemList = new ArrayList<>();

        while(itemIterator.hasNext()) {
            Item item = itemIterator.next();

            if(item.getRegistryName().getNamespace().equals("minecraft")) { //Only Vanilla Items
                if(item instanceof BlockItem) {
                    BlockItem blockItem = (BlockItem) item;Block block = blockItem.getBlock();
                    Block.Properties blockProp = Block.Properties.from(block);
                    try {
                        Field hardness = Block.Properties.class.getDeclaredField("hardness");
                        hardness.setAccessible(true);
                        Field resistance = Block.Properties.class.getDeclaredField("resistance");
                        resistance.setAccessible(true);
                        if((float)hardness.get(blockProp) != -1) {
                            if((float)resistance.get(blockProp) < 3600000.0F) { //remove all Unbreakeable aka unobtaineable Items
                                itemList.add(item);
                            }
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                itemList.add(item);
            }
        }
        return itemList;
    }
    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        return new PacketItemSyncResponse(toFind, getEnergy());
    }
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if(toFind != null) {
            compound.putString("itemRegistryName", toFind.getRegistryName().toString());
        }
        return compound;
    }
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        String registryName = compound.getString("itemRegistryName");
        Iterator<Item> itemIterator = Registry.ITEM.iterator();
        while(itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if(item.getRegistryName().toString().equals(registryName)) {
                toFind = item;
            }
        }
    }
}
