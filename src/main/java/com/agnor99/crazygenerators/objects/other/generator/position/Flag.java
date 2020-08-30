package com.agnor99.crazygenerators.objects.other.generator.position;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class Flag {

    public ServerPlayerEntity player;
    public String playerName;
    int x,y,z;
    public Flag(ServerPlayerEntity player, int x, int y, int z) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayerEntity player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
