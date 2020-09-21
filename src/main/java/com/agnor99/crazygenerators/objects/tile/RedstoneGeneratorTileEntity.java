package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.objects.container.RedstoneGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class RedstoneGeneratorTileEntity extends GeneratorTileEntity{
    boolean shouldReadRedstone = true;
    public int targetRedstoneData0;
    public int targetRedstoneData1;
    public int lastRedstoneData;
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
        int currentRedstoneData = world.getRedstonePowerFromNeighbors(pos);
        int[] additionalRedstoneData = new int[4];
        for(int i = 2; i < 6;i++) { //horizontalRotation
            additionalRedstoneData[i-2] = world.getRedstonePower(pos, Direction.byIndex(i));
        }

        if (lastRedstoneData == targetRedstoneData0 && targetRedstoneData1 == currentRedstoneData) {
            addEnergy(100000);
            resetTarget();
        }

        lastRedstoneData = currentRedstoneData;
    }

    private void resetTarget() {
        targetRedstoneData0 = 0;
        targetRedstoneData1 = 0;
    }

    public void generateTarget() {
        Random r = new Random();
        targetRedstoneData0 = r.nextInt(14)+1;
        targetRedstoneData1 = 0;
        while(targetRedstoneData1 == 0 || targetRedstoneData1 == targetRedstoneData0) {
            targetRedstoneData1 = r.nextInt(14)+1;
        }
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RedstoneGeneratorContainer(id, player, this);
    }

    @Override
    protected boolean shouldTickIntern() {
        return !world.isRemote() && targetRedstoneData0 != 0 && targetRedstoneData1 != 0;
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        return new PacketRedstoneSyncResponse(getEnergy(), targetRedstoneData0, targetRedstoneData1);
    }
}
