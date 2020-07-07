package com.agnor99.crazygenerators.objects.other;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class GeneratorEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {
    public GeneratorEnergyStorage(int capacity) {
        super(capacity,0, capacity);

    }
    public CompoundNBT serializeNBT() {
        CompoundNBT energy = new CompoundNBT();
        energy.putInt("energy", getEnergyStored());
        return energy;
    }
    public void setEnergy(int newEnergy) {
        energy = newEnergy;
    }
    public void setCapacity(int newCapacity) {
        capacity = newCapacity;
    }
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(nbt.getInt("energy"));
    }
    public void addEnergy(int energy) {
        this.energy = Math.min(energy+this.energy, capacity);
    }
    public void consumeEnergy(int energy) {
        this.energy -= energy;
        if(this.energy < 0) {
            this.energy = 0;
        }
    }

}
