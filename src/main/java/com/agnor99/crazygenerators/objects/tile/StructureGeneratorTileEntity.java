package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureDataPacket;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketStructureSyncResponse;
import com.agnor99.crazygenerators.objects.blocks.StructureConnectorBlock;
import com.agnor99.crazygenerators.objects.blocks.StructureCoreBlock;
import com.agnor99.crazygenerators.objects.blocks.StructureOrbBlock;
import com.agnor99.crazygenerators.objects.container.StructureGeneratorContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;


public class StructureGeneratorTileEntity extends GeneratorTileEntity{
    public static final int STRUCTURE_NONE = 0;
    public static final int STRUCTURE_CONNECTOR = 1;
    public static final int STRUCTURE_CORE = 2;
    public static final int STRUCTURE_ORB = 3;
    public int[][] structureTarget = new int[5][5];
    public StructureGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);

    }
    public StructureGeneratorTileEntity() {
        this(TileInit.STRUCTURE_GENERATOR.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.structure_generator");
    }

    @Override
    public void tick() {
        super.tick();
        if(!shouldTickIntern()) return;
        checkMap();
    }

    public void updateMap() {
        Random random = new Random();
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                structureTarget[x][z] = random.nextInt(3)+1;
            }
        }
        sendToAllLooking(new StructureDataPacket(structureTarget));
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new StructureGeneratorContainer(id, player, this);
    }

    private void checkMap() {
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                if(structureTarget[x][z] != getIntForBlockPos(new BlockPos(pos).south(z-2).east(x-2).up(1))) {
                    return;
                }
            }
        }
        addEnergy(100000);
        resetStructure();
        removeBlocksFromWorld();
    }

    private int getIntForBlockPos(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if(block instanceof StructureConnectorBlock) return STRUCTURE_CONNECTOR;
        if(block instanceof StructureCoreBlock) return STRUCTURE_CORE;
        if(block instanceof StructureOrbBlock) return STRUCTURE_ORB;
        return STRUCTURE_NONE;
    }

    private void resetStructure() {
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                structureTarget[x][z] = STRUCTURE_NONE;
            }
        }
    }

    private void removeBlocksFromWorld() {
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                world.removeBlock(pos.south(z-2).east(x-2).up(1), false);
            }
        }
    }

    @Override
    protected boolean shouldTickIntern() {
        if(world.isRemote()) return false;
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                if(structureTarget[x][z] == STRUCTURE_NONE) return false;
            }
        }
        return true;
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        return new PacketStructureSyncResponse(getEnergy(), structureTarget);
    }
}
