package com.agnor99.crazygenerators.container;

import com.agnor99.crazygenerators.init.BlockInit;
import com.agnor99.crazygenerators.init.ContainerInit;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.awt.*;
import java.util.Objects;

public class QuestionGeneratorContainer extends Container {
    public final QuestionGeneratorTileEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    public QuestionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowID, playerInventory, getTileEntity(playerInventory, data));
    }

    public QuestionGeneratorContainer(final int windowID, final PlayerInventory playerInventory, final QuestionGeneratorTileEntity tileEntity) {
        super(ContainerInit.QUESTION_GENERATOR.get(), windowID);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        final int SLOTDIFF = 18;

        Point loadingSlot = new Point(152,18);
        addSlot(new Slot(tileEntity, 0, loadingSlot.x, loadingSlot.y));

        Point playerStartSlot = new Point(8,102);
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + row*9 + column, playerStartSlot.x + column*SLOTDIFF, playerStartSlot.y + row*SLOTDIFF));
            }
        }

        Point playerStartHotbarSlot = new Point(8,160);
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, playerStartHotbarSlot.x + column*SLOTDIFF, playerStartHotbarSlot.y));
        }

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


    }

    private static QuestionGeneratorTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileAtPos instanceof QuestionGeneratorTileEntity) {
            return (QuestionGeneratorTileEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile Entity is not correct" + tileAtPos);
    }

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.question_generator);
    }

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
