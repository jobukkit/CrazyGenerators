package com.agnor99.crazygenerators.objects.other.generator.position;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class Flag {

    public List<ServerPlayerEntity> players;
    public ServerPlayerEntity closestPlayer;
    public String playerName;
    int smallestDistance;
    int x,y,z;
    public Flag() {
        players = new ArrayList<>();
    }

    public List<ServerPlayerEntity> getPlayers() {
        return players;
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

    public void setSmallestDistance(int d) {
        smallestDistance = d;
    }
    public int getSmallestDistance() {
        return smallestDistance;
    }
}
