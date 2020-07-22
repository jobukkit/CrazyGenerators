package com.agnor99.crazygenerators.objects.other;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class LoadingSlot extends Slot {
    public LoadingSlot(IInventory inventory, int index, int slotx, int sloty) {
        super(inventory, index, slotx, sloty);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {

        return itemStack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        return 1;
    }
}
