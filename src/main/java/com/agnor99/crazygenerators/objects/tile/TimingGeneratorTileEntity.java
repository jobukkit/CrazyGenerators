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

import java.util.Random;

public class TimingGeneratorTileEntity extends GeneratorTileEntity{
    public static final int TICKS_TO_CLICK = 20; // 1 Second is the box green
    public static final int MAX_TICK_DELAY_FOR_PING = 40; // max Ping of 2 seconds
    public static final int MIN_TICK_DELAY = 200; // every 10 to 25 seconds the
    public static final int MAX_TICK_DELAY = 500;

    int tickToUnlock = Integer.MIN_VALUE;
    int multiplier = 1;


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
        if(tickToUnlock + TICKS_TO_CLICK + MAX_TICK_DELAY_FOR_PING == tick) {
            generateUnlockTime();
            multiplier = 1;
        }


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
        generateUnlockTime();
        multiplier = 1;
        return new PacketTimingSyncResponse(getEnergy(),false);
    }

    public int addClickEnergy(int ping){
        int energy = calculateEnergy(ping);
        addEnergy(energy);
        return energy;
    }

    int calculateEnergy(int ping) {
        int tickDelay = calcDelay(ping);
        int energy = 0;
        if (tickDelay < TICKS_TO_CLICK && tickDelay >= 0) {
            energy = (int) (15000 - 10000*Math.sqrt(tickDelay/10.0d));
            energy *= multiplier;
            multiplier++;
            multiplier = Math.min(multiplier, 8);
        } else {
            multiplier = 1;
        }
        return energy;
    }
    public int calcDelay(int ping){
        int pingInTicks = ping/50;
        return tick-pingInTicks-tickToUnlock;
    }
    public void generateUnlockTime() {
        setTickToUnlock(new Random().nextInt(MAX_TICK_DELAY-MIN_TICK_DELAY)+MIN_TICK_DELAY+tick);
    }

    public int getTickToUnlock() {
        return tickToUnlock;
    }

    public void setTickToUnlock(int tickToUnlock) {
        this.tickToUnlock = tickToUnlock;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

}
