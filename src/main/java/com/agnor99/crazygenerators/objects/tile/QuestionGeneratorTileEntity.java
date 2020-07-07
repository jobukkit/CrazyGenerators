package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class QuestionGeneratorTileEntity extends GeneratorTileEntity{



    public QuestionGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }
    public QuestionGeneratorTileEntity() {
        this(TileInit.QUESTION_GENERATOR.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.question_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new QuestionGeneratorContainer(id, player, this);
    }
}
