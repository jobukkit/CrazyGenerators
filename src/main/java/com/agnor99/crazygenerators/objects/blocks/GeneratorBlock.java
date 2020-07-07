package com.agnor99.crazygenerators.objects.blocks;

import net.minecraft.block.BlockState;

public abstract class GeneratorBlock extends GeneratorPlaceHolderBlock{
    public GeneratorBlock(String registryName) {
        super(registryName);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
