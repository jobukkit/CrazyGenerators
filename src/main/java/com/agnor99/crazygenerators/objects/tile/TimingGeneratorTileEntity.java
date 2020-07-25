package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.TimingGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketTimingSyncResponse;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TimingGeneratorTileEntity extends GeneratorTileEntity{

    public TimingGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);

    }
    public TimingGeneratorTileEntity() {
        this(TileInit.TIMING_GENERATOR.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.timing_generator");
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TimingGeneratorContainer(id, player, this);
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        if(players.size() > 1) {
            return new PacketTimingSyncResponse(getEnergy(), true);
        }
        return new PacketTimingSyncResponse(getEnergy(),false);
    }

}
