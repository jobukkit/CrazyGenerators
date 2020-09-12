package com.agnor99.crazygenerators.objects.blocks;


import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RedstoneGeneratorBlock extends GeneratorBlock {
    public RedstoneGeneratorBlock() {
        super("redstone_generator");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileInit.REDSTONE_GENERATOR.get().create();
    }
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote) {
            TileEntity tile = world.getTileEntity(blockPos);
            if(tile instanceof RedstoneGeneratorTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (RedstoneGeneratorTileEntity)tile, blockPos);
                return ActionResultType.SUCCESS;
            }else{
                return ActionResultType.FAIL;
            }
        }else {
            return ActionResultType.SUCCESS;
        }
    }
    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos blockPos, @Nullable Direction directionFromNeighborToThis)
    {
        return true;
    }
}
