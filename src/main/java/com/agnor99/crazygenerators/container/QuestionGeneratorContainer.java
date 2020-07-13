package com.agnor99.crazygenerators.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;


public class QuestionGeneratorContainer extends GeneratorContainer {

    public QuestionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public QuestionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(ContainerInit.QUESTION_GENERATOR.get(), windowID, playerInventory, tileEntity);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.question_generator);
    }

    @Override
    protected void tracking() {
        super.tracking();
        QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) tileEntity;

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return qgte.getTipsAvailable();
            }

            @Override
            public void set(int value) {
                qgte.setTipsAvailable(value);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return qgte.getQuestionLevel();
            }

            @Override
            public void set(int value) {
                qgte.setQuestionLevel(value);
            }
        });
    }
}
