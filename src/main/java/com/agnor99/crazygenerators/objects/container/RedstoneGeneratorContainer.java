package com.agnor99.crazygenerators.objects.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;


public class RedstoneGeneratorContainer extends GeneratorContainer {

    public RedstoneGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public RedstoneGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.REDSTONE_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.redstone_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        RedstoneGeneratorTileEntity rgte = (RedstoneGeneratorTileEntity) tileEntity;

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return rgte.targetRedstoneData0;
            }

            @Override
            public void set(int i) {
                rgte.targetRedstoneData0 = i;
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return rgte.targetRedstoneData1;
            }

            @Override
            public void set(int i) {
                rgte.targetRedstoneData1 = i;
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return rgte.lastRedstoneData;
            }

            @Override
            public void set(int i) {
                rgte.lastRedstoneData = i;
            }
        });
    }
}
