package com.agnor99.crazygenerators.objects.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;


public class StructureGeneratorContainer extends GeneratorContainer {

    public StructureGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public StructureGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.STRUCTURE_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.structure_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        StructureGeneratorTileEntity gte = (StructureGeneratorTileEntity) tileEntity;
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                trackInt(IntReferenceHolder.create(gte.structureTarget[x], z));
            }
        }
    }
}
