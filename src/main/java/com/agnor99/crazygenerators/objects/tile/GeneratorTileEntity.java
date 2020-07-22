package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.objects.other.GeneratorEnergyStorage;
import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.objects.blocks.QuestionGeneratorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GeneratorTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    int tick;
    private NonNullList<ItemStack> itemStackToLoad = NonNullList.withSize(1, ItemStack.EMPTY);
    public List<ServerPlayerEntity> players = new ArrayList<ServerPlayerEntity>();

    private IItemHandlerModifiable items = createHandler();
    public GeneratorEnergyStorage energy = createEnergy();
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
        return new QuestionGeneratorContainer(id, player, this);
    }

    public int getTick() {
        return tick;
    }
    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public int getSizeInventory() {
        return itemStackToLoad.size();
    }

    public void tick() {
        if(world.isRemote()) return;
        markDirty();
        tick++;
        sendPower();
        powerItem();
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
    private void powerItem() {
        try {
            ItemStack itemToLoad = itemStackToLoad.get(0);
            if(itemToLoad == null) return;
            if(itemToLoad.getCount() != 1) return;
            AtomicInteger currentEnergy = new AtomicInteger(energy.getEnergyStored());

            itemToLoad.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                if (handler.canReceive()) {

                    int received = handler.receiveEnergy(Math.min(currentEnergy.get(), 10000), false);

                    currentEnergy.addAndGet(-received);
                    ((GeneratorEnergyStorage) energy).consumeEnergy(received);
                }
            });

        }catch(ArrayIndexOutOfBoundsException e) {

        }
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
    public void addEnergy(int energy) {
        this.energy.addEnergy(energy);
    }
    public void setEnergy(int energy) {
        this.energy.setEnergy(energy);
    }
    public int getEnergy() {
        return energy.getEnergyStored();
    }
    public abstract PacketAbstractSyncResponse generateSyncPacket();



    @Override
    public void openInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            players.add((ServerPlayerEntity)player);
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if(!player.isSpectator()) {
            players.remove(player);
        }
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
