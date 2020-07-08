package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.other.GeneratorEnergyStorage;
import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.objects.blocks.QuestionGeneratorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GeneratorTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    int tick;
    private NonNullList<ItemStack> itemStackToLoad = NonNullList.withSize(1, ItemStack.EMPTY);
    protected int numPlayersUsing;

    private IItemHandlerModifiable items = createHandler();
    private GeneratorEnergyStorage energy = createEnergy();
    private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(()-> energy);


    public GeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return itemStackToLoad;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        itemStackToLoad = nonNullList;
    }

    @Override
    protected abstract ITextComponent getDefaultName();

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new QuestionGeneratorContainer(id, player, this);//TODO: Continue Refactor
    }

    @Override
    public int getSizeInventory() {
        return itemStackToLoad.size();
    }

    public void tick() {
        if(world.isRemote()) return;
        markDirty();
        tick++;

        if(tick%4 == 0) {;
            energy.addEnergy(10000);
        }
        sendPower();
    }

    public void sendPower() {
        energyHandler.ifPresent(energy -> {
            AtomicInteger currentEnergy = new AtomicInteger(energy.getEnergyStored());
            if(currentEnergy.get() > 0) {
                for (Direction direction : Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        te.getCapability(CapabilityEnergy.ENERGY, direction).ifPresent(handler -> {
                            if (handler.canReceive()) {

                                int received = handler.receiveEnergy(Math.min(currentEnergy.get(), energy.getMaxEnergyStored()), false);

                                currentEnergy.addAndGet(-received);
                                ((GeneratorEnergyStorage) energy).consumeEnergy(received);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("tick", tick);
        if(!checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, itemStackToLoad);
        }
        compound.putInt("energy", energy.getEnergyStored());
        return compound;
    }


    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        tick = compound.getInt("tick");
        itemStackToLoad = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        if(!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, itemStackToLoad);
        }
        energy.setEnergy(tick = compound.getInt("energy"));
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if(id == 1) {
            numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if(!player.isSpectator()) {

            if(this.numPlayersUsing < 0) {
                numPlayersUsing = 0;
            }
            numPlayersUsing++;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            numPlayersUsing--;
            this.onOpenOrClose();
        }
    }
    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if(block instanceof QuestionGeneratorBlock) {
            world.addBlockEvent(pos, block, 1, numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(pos, block);
        }
    }
    public static int getPlayersUsing(IBlockReader reader, BlockPos pos) {
        BlockState blockState = reader.getBlockState(pos);
        if(blockState.hasTileEntity()) {
            TileEntity tileEntity = reader.getTileEntity(pos);
            if(tileEntity instanceof  QuestionGeneratorTileEntity) {
                return ((QuestionGeneratorTileEntity) tileEntity).numPlayersUsing;
            }
        }
        return 0;
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if(this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction Direction) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        if(cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, Direction);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        if(cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap);
    }


    private IItemHandlerModifiable createHandler() {
        return new InvWrapper(this);
    }
    private GeneratorEnergyStorage createEnergy() {
        return new GeneratorEnergyStorage(2000000);
    }

    @Override
    public void remove() {
        super.remove();
        if(itemHandler != null) {
            itemHandler.invalidate();
        }
    }
}
