package com.agnor99.crazygenerators.objects.blocks;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class GeneratorBlock extends GeneratorPlaceHolderBlock{
    public GeneratorBlock(String registryName) {
        super(registryName);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if(tileEntity instanceof GeneratorTileEntity) {
                InventoryHelper.dropItems(world, blockPos, ((GeneratorTileEntity) tileEntity).getItems());
            }
            tileEntity.remove();
        }
    }
}
