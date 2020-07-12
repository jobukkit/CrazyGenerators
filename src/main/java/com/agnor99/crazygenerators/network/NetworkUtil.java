package com.agnor99.crazygenerators.network;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketAnswer;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketGiveQuestion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkUtil {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CrazyGenerators.MOD_ID,"generator_packets"), () -> "1.0", s->true, s->true);

        INSTANCE.registerMessage(
                nextID(),
                PacketGiveQuestion.class,
                PacketGiveQuestion::toBytes,
                PacketGiveQuestion::new,
                PacketGiveQuestion::handle
                );
        INSTANCE.registerMessage(
                nextID(),
                PacketAnswer.class,
                PacketAnswer::toBytes,
                PacketAnswer::new,
                PacketAnswer::handle
        );
    }

}
