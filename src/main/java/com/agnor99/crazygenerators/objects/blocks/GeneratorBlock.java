package com.agnor99.crazygenerators.objects.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public abstract class GeneratorBlock extends GeneratorPlaceHolderBlock{
    public GeneratorBlock(String registryName) {
        super(registryName);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
