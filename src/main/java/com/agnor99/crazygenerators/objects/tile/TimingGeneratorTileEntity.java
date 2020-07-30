package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.TimingGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketTimingSyncResponse;
import com.agnor99.crazygenerators.objects.other.generator.timing.ButtonData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.Random;

public class TimingGeneratorTileEntity extends GeneratorTileEntity{
    public static final int TICKS_TO_CLICK = 30; // 1.5 Second is the box green
    public static final int MAX_TICK_DELAY_FOR_PING = 40; // max Ping of 2 seconds
    public static final int MIN_TICK_DELAY = 100; // every 5 to 15 seconds the
    public static final int MAX_TICK_DELAY = 300;

    int tickToUnlock = Integer.MIN_VALUE;
    int multiplier = 1;
    public Point buttonPosition = new Point();

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
        if(world.isRemote()) return;
        super.tick();
        if(tickToUnlock + TICKS_TO_CLICK + MAX_TICK_DELAY_FOR_PING == tick) {
            generateUnlockData();
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
        generateUnlockData();
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
            energy = (int) (15000 - 10000*Math.sqrt(tickDelay/15.0d));
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
    public void generateUnlockData() {
        Random r = new Random();
        setTickToUnlock(r.nextInt(MAX_TICK_DELAY-MIN_TICK_DELAY)+MIN_TICK_DELAY+tick);
        buttonPosition = new Point(r.nextInt(ButtonData.buttonPlacePoint2WithoutButton.x-ButtonData.buttonPlacePoint1.x)+ButtonData.buttonPlacePoint1.x,
                                        r.nextInt(ButtonData.buttonPlacePoint2WithoutButton.y-ButtonData.buttonPlacePoint1.y)+ButtonData.buttonPlacePoint1.y);

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
