package com.agnor99.crazygenerators.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.PongGeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;

import java.util.List;


public class PongGeneratorContainer extends GeneratorContainer {

    public PongGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public PongGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.PONG_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.pong_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        PongGeneratorTileEntity pgte = (PongGeneratorTileEntity) tileEntity;
        List<IntReferenceHolder> references = pgte.game.getIntReferenceHolder();
        for(IntReferenceHolder referenceHolder: references){
            trackInt(referenceHolder);
        }
    }
}
