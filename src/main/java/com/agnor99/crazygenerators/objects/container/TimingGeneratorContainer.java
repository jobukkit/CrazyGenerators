package com.agnor99.crazygenerators.objects.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;


public class TimingGeneratorContainer extends GeneratorContainer {

    public TimingGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public TimingGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.TIMING_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.timing_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        TimingGeneratorTileEntity tgte = (TimingGeneratorTileEntity) tileEntity;
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return tgte.getTickToUnlock();
                    }

                    @Override
                    public void set(int i) {
                        tgte.setTickToUnlock(i);
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return tgte.getMultiplier();
                    }

                    @Override
                    public void set(int i) {
                        tgte.setMultiplier(i);
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return (int) tgte.buttonPosition.getX();
                    }

                    @Override
                    public void set(int i) {
                        tgte.buttonPosition.x = i;
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return (int) tgte.buttonPosition.getY();
                    }

                    @Override
                    public void set(int i) {
                        tgte.buttonPosition.y = i;
                    }
                }
        );
    }
}
