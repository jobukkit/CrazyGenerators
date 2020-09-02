package com.agnor99.crazygenerators.objects.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;


public class PositionGeneratorContainer extends GeneratorContainer {

    public PositionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public PositionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.POSITION_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.position_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        PositionGeneratorTileEntity pgte = (PositionGeneratorTileEntity) tileEntity;
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pgte.flag.getX();
                    }

                    @Override
                    public void set(int i) {
                        pgte.flag.setX(i);
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pgte.flag.getY();
                    }

                    @Override
                    public void set(int i) {
                        pgte.flag.setY(i);
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pgte.flag.getZ();
                    }

                    @Override
                    public void set(int i) {
                        pgte.flag.setZ(i);
                    }
                }
        );
        trackInt(
                new IntReferenceHolder() {
                    @Override
                    public int get() {
                        return pgte.flag.getSmallestDistance();
                    }
                    @Override
                    public void set(int i) {
                        pgte.flag.setSmallestDistance(i);
                    }
                }
        );
    }
}
