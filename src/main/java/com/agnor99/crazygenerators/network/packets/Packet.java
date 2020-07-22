package com.agnor99.crazygenerators.network.packets;

import com.agnor99.crazygenerators.objects.tile.GeneratorTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet {
    void toBytes(PacketBuffer buf);
    default void handle(Supplier<NetworkEvent.Context> context) {
        if(isValid(context)){
            doWork(context);
        }
    }


    default boolean checkBlockPos(BlockPos pos, ServerWorld world) {
        return world.isBlockLoaded(pos);
    }

    default boolean checkBlockPosWithPlayer(BlockPos pos,  ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        if(checkBlockPos(pos, world)) {
            if(world.getTileEntity(pos) instanceof GeneratorTileEntity) {
                GeneratorTileEntity te = (GeneratorTileEntity) world.getTileEntity(pos);
                return te.players.contains(player);
            }
        }
        return false;
    }
    boolean isValid(Supplier<NetworkEvent.Context> context);
    void doWork(Supplier<NetworkEvent.Context> context);
}
