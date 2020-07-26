package com.agnor99.crazygenerators.network.packets.timing_generator;

import com.agnor99.crazygenerators.client.gui.TimingGeneratorScreen;
import com.agnor99.crazygenerators.network.packets.ServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketButtonPressResponse implements ServerPacket {

    final int delay;
    final int energyAdded;

    public PacketButtonPressResponse(int delay, int energyAdded) {
        this.delay = delay;
        this.energyAdded = energyAdded;
    }

    public PacketButtonPressResponse(PacketBuffer buf) {
        delay = buf.readInt();
        energyAdded = buf.readInt();
    }
    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(delay);
        buf.writeInt(energyAdded);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> context) {
        TimingGeneratorScreen screen = (TimingGeneratorScreen) Minecraft.getInstance().currentScreen;
        if(delay >= 0) {
            screen.lastDelay = delay;
            screen.lastEnergyAdded = energyAdded;
        }else{
            screen.lastDelay = Integer.MIN_VALUE;
            screen.lastEnergyAdded = Integer.MIN_VALUE;
        }
        context.get().setPacketHandled(true);
    }
}
