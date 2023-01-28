package com.agnor99.crazygenerators.network;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.network.packets.item_generator.ItemPacket;
import com.agnor99.crazygenerators.network.packets.item_generator.RequestItemPacket;
import com.agnor99.crazygenerators.network.packets.position_generator.ClosestPlayerPacket;
import com.agnor99.crazygenerators.network.packets.position_generator.NewCoordsPacket;
import com.agnor99.crazygenerators.network.packets.position_generator.WayPointChatMessagePacket;
import com.agnor99.crazygenerators.network.packets.question_generator.*;
import com.agnor99.crazygenerators.network.packets.redstone_generator.UpdateSequencePacket;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureDataPacket;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureGenerationPacket;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureGetPacket;
import com.agnor99.crazygenerators.network.packets.sync.*;
import com.agnor99.crazygenerators.network.packets.timing_generator.PacketButtonPress;
import com.agnor99.crazygenerators.network.packets.timing_generator.PacketButtonPressResponse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkUtil {

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CrazyGenerators.MOD_ID,"generator_packets"), () -> "1.0", s->true, s->true);;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(
                nextID(),
                PacketRequestSync.class,
                PacketRequestSync::toBytes,
                PacketRequestSync::new,
                PacketRequestSync::handle
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
                PacketPositionSyncResponse.class,
                PacketPositionSyncResponse::toBytes,
                PacketPositionSyncResponse::new,
                PacketPositionSyncResponse::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketItemSyncResponse.class,
                PacketItemSyncResponse::toBytes,
                PacketItemSyncResponse::new,
                PacketItemSyncResponse::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketRedstoneSyncResponse.class,
                PacketRedstoneSyncResponse::toBytes,
                PacketRedstoneSyncResponse::new,
                PacketRedstoneSyncResponse::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                PacketStructureSyncResponse.class,
                PacketStructureSyncResponse::toBytes,
                PacketStructureSyncResponse::new,
                PacketStructureSyncResponse::handle
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


        INSTANCE.registerMessage(
                nextID(),
                NewCoordsPacket.class,
                NewCoordsPacket::toBytes,
                NewCoordsPacket::new,
                NewCoordsPacket::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                ClosestPlayerPacket.class,
                ClosestPlayerPacket::toBytes,
                ClosestPlayerPacket::new,
                ClosestPlayerPacket::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                WayPointChatMessagePacket.class,
                WayPointChatMessagePacket::toBytes,
                WayPointChatMessagePacket::new,
                WayPointChatMessagePacket::handle
        );


        INSTANCE.registerMessage(
                nextID(),
                ItemPacket.class,
                ItemPacket::toBytes,
                ItemPacket::new,
                ItemPacket::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                RequestItemPacket.class,
                RequestItemPacket::toBytes,
                RequestItemPacket::new,
                RequestItemPacket::handle
        );


        INSTANCE.registerMessage(
                nextID(),
                UpdateSequencePacket.class,
                UpdateSequencePacket::toBytes,
                UpdateSequencePacket::new,
                UpdateSequencePacket::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                StructureGenerationPacket.class,
                StructureGenerationPacket::toBytes,
                StructureGenerationPacket::new,
                StructureGenerationPacket::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                StructureGetPacket.class,
                StructureGetPacket::toBytes,
                StructureGetPacket::new,
                StructureGetPacket::handle
        );
        INSTANCE.registerMessage(
                nextID(),
                StructureDataPacket.class,
                StructureDataPacket::toBytes,
                StructureDataPacket::new,
                StructureDataPacket::handle
        );
    }
}
