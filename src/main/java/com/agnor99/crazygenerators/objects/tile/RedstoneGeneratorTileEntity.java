package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.network.packets.redstone_generator.SequencePacket;
import com.agnor99.crazygenerators.objects.container.RedstoneGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedstoneGeneratorTileEntity extends GeneratorTileEntity{
    public boolean[] targetRedstoneData;
    public List<Integer> redstoneMarker = new ArrayList();
    boolean shouldReadRedstone = true;
    boolean lastRedstoneData = false;
    public RedstoneGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public RedstoneGeneratorTileEntity() {
        this(TileInit.REDSTONE_GENERATOR.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.redstone_generator");
    }

    @Override
    public void tick() {
        super.tick();
        if(!shouldTickIntern()) return;
        shouldReadRedstone = !shouldReadRedstone;
        if(!shouldReadRedstone) return;//every 2nd tick because 20 gameTicks = 10 RedstoneTicks//
        if(targetRedstoneData == null) return;
        boolean currentRedstoneData = !(world.getRedstonePowerFromNeighbors(pos)==0);
        updateMarker(currentRedstoneData);
    }

    private void updateMarker(boolean currentRedstoneData) {
        List<Integer> newMarker = new ArrayList();
        for(int marker: redstoneMarker) {
            if(targetRedstoneData[marker+1] == currentRedstoneData) {
                newMarker.add(marker+1);
            }
        }
        boolean removeAllMarker = false;
        for(int marker: newMarker) {
            if(marker == 19) {
                addEnergy(50000);
                removeAllMarker = true;
            }
        }
        if(removeAllMarker) {
            newMarker.clear();
        }else{
            if(!lastRedstoneData && currentRedstoneData) {
                newMarker.add(0);
            }
        }
        redstoneMarker = newMarker;
    }

    public void generateTarget() {
        List<Integer> targetData = new ArrayList();
        Random r = new Random();
        while(getSum(targetData) < 20) {
            targetData.add(r.nextInt(8) + 2);
        }
        int sum = getSum(targetData);
        if(sum > 20) {
            targetData.remove(targetData.size()-1);
            targetData.add(20 - getSum(targetData));
        }
        if(targetData.get(targetData.size()-1) < 2) {
            targetData.remove(targetData.size()-1);
            targetData.remove(targetData.size()-1);
            targetData.add(20-getSum(targetData));
        }
        targetRedstoneData = intListToBooleanArray(targetData);
        sendToAllLooking(new SequencePacket(targetRedstoneData));
    }

    private int getSum(List<Integer> list) {
         int sum = 0;
         for(int value: list) {
             sum += value;
         }
         return sum;
    }
    private boolean[] intListToBooleanArray(List<Integer> list) {
        boolean[] retValues = new boolean[20];
        int iterationValue = 0;
        boolean isHigh = true;
        for(int intValue: list) {
            for(int i = 0; i < intValue; i++) {
                try {
                    int index = iterationValue+i;
                    retValues[index] = isHigh;
                }catch(Exception e) {
                    int c = 10;
                    System.out.println("test");
                }
            }
            iterationValue +=intValue;
            isHigh = !isHigh;
        }
        return retValues;
    }
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RedstoneGeneratorContainer(id, player, this);
    }

    @Override
    protected boolean shouldTickIntern() {
        return !world.isRemote();
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        return new PacketRedstoneSyncResponse(getEnergy(), targetRedstoneData);
    }
}
