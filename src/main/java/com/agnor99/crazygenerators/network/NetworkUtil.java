package com.agnor99.crazygenerators.network;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.question_generator.*;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketQuestionSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketRequestSync;
import com.agnor99.crazygenerators.network.packets.sync.PacketTimingSyncResponse;
import com.agnor99.crazygenerators.network.packets.timing_generator.PacketButtonPress;
import com.agnor99.crazygenerators.network.packets.timing_generator.PacketButtonPressResponse;
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
                PacketAnswerResponse.class,
                PacketAnswerResponse::toBytes,
                PacketAnswerResponse::new,
                PacketAnswerResponse::handle
                );

        INSTANCE.registerMessage(
                nextID(),
                PacketAnswer.class,
                PacketAnswer::toBytes,
                PacketAnswer::new,
                PacketAnswer::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketRequestSync.class,
                PacketRequestSync::toBytes,
                PacketRequestSync::new,
                PacketRequestSync::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketHint.class,
                PacketHint::toBytes,
                PacketHint::new,
                PacketHint::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketHintRequest.class,
                PacketHintRequest::toBytes,
                PacketHintRequest::new,
                PacketHintRequest::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketTimeOut.class,
                PacketTimeOut::toBytes,
                PacketTimeOut::new,
                PacketTimeOut::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketQuestionSyncResponse.class,
                PacketQuestionSyncResponse::toBytes,
                PacketQuestionSyncResponse::new,
                PacketQuestionSyncResponse::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketTimingSyncResponse.class,
                PacketTimingSyncResponse::toBytes,
                PacketTimingSyncResponse::new,
                PacketTimingSyncResponse::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketButtonPress.class,
                PacketButtonPress::toBytes,
                PacketButtonPress::new,
                PacketButtonPress::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketButtonPressResponse.class,
                PacketButtonPressResponse::toBytes,
                PacketButtonPressResponse::new,
                PacketButtonPressResponse::handle
        );
    }
}
