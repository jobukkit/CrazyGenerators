package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.objects.container.StructureGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.Random;

public class StructureGeneratorTileEntity extends GeneratorTileEntity{

    public StructureGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);

    }
    public StructureGeneratorTileEntity() {
        this(TileInit.STRUCTURE_GENERATOR.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.structure_generator");
    }

    @Override
    public void tick() {
        super.tick();
        if(!shouldTickIntern()) return;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new StructureGeneratorContainer(id, player, this);
    }


    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        return new PacketStructureSyncResponse(getEnergy());
    }
}
