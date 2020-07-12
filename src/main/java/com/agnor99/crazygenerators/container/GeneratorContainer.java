package com.agnor99.crazygenerators.container;

import com.agnor99.crazygenerators.objects.other.GeneratorEnergyStorage;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

public abstract class GeneratorContainer extends Container {
    protected final GeneratorTileEntity tileEntity;
    protected final IWorldPosCallable canInteractWithCallable;

    public GeneratorTileEntity getTileEntity() {
        return tileEntity;
    }

    public GeneratorContainer(@Nullable ContainerType<?> type, final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(type ,windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public GeneratorContainer(@Nullable ContainerType<?> type, final int windowID, final PlayerInventory playerInventory, final GeneratorTileEntity tileEntity) {
        super(type, windowID);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        addSlots2Container(playerInventory);
        tracking();

    }
    private void addSlots2Container(PlayerInventory playerInventory) {
        final int SLOTDIFF = 18;

        Point loadingSlot = new Point(152,18);
        Point playerStartSlot = new Point(8,102);
        Point playerStartHotbarSlot = new Point(8,160);

        addSlot(new Slot(tileEntity, 0, loadingSlot.x, loadingSlot.y));


        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + row*9 + column, playerStartSlot.x + column*SLOTDIFF, playerStartSlot.y + row*SLOTDIFF));
            }
        }

        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, playerStartHotbarSlot.x + column*SLOTDIFF, playerStartHotbarSlot.y));
        }
    }

    protected void tracking() {
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergy();
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((GeneratorEnergyStorage)h).setEnergy(value));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getCapacity();
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((GeneratorEnergyStorage)h).setEnergy(value));
            }
        });
    }
    protected static GeneratorTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileAtPos instanceof GeneratorTileEntity) {
            return (GeneratorTileEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile Entity is not correct" + tileAtPos);
    }

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }
    public int getCapacity() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }
    @Override
    public abstract boolean canInteractWith(PlayerEntity playerIn);

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotIndex == 0) {
                if (!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}
