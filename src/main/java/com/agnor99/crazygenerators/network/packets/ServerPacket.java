package com.agnor99.crazygenerators.network.packets;

import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface ServerPacket extends Packet {

    @Override
    default boolean isValid(Supplier<NetworkEvent.Context> context) {
        return true;
    }

    default GeneratorTileEntity getTileEntity() {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(screen instanceof GeneratorScreen) {
            GeneratorScreen generatorScreen = (GeneratorScreen) screen;
            return generatorScreen.getContainer().getTileEntity();
        }
        return null;
    }
}
