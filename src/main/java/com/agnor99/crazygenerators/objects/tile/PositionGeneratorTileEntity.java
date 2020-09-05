package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.Packet;
import com.agnor99.crazygenerators.network.packets.position_generator.*;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketPositionSyncResponse;
import com.agnor99.crazygenerators.objects.container.PositionGeneratorContainer;
import com.agnor99.crazygenerators.objects.other.generator.position.Flag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.Random;


public class PositionGeneratorTileEntity extends GeneratorTileEntity{

    public Flag flag;

    @OnlyIn(Dist.CLIENT)
    private EntityType<SlimeEntity> type = EntityType.SLIME;
    @OnlyIn(Dist.CLIENT)
    private SlimeEntity marker;

    public PositionGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        flag = new Flag();


    }
    public PositionGeneratorTileEntity() {
        this(TileInit.POSITION_GENERATOR.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.position_generator");
    }

    @Override
    public void tick() {
        if(world.isRemote()) return;
        updateClosestPlayer();
    }

    private void updateClosestPlayer() {
        if(flag.players.size() == 0) {
            updateFlag();
        }
        ServerPlayerEntity oldClosestPlayer = flag.closestPlayer;
        flag.closestPlayer = null;
        flag.setSmallestDistance(Integer.MAX_VALUE);

        boolean shouldUpdateFlag = false;
        for(ServerPlayerEntity player:flag.players) {
            double distance = (int)player.getPosition().distanceSq(new BlockPos(flag.getX(), flag.getY(), flag.getZ()));
            distance = Math.sqrt(distance);
            if(distance < flag.getSmallestDistance()) {
                flag.setSmallestDistance((int)distance);
                flag.closestPlayer = player;
            }
            if(distance < 5) {
                addEnergy(10000);
                shouldUpdateFlag = true;
            }
        }
        if(flag.closestPlayer != oldClosestPlayer) {
            Packet packet;
            if(flag.closestPlayer == null) {
                packet = new ClosestPlayerPacket("");
            }else {
                packet = new ClosestPlayerPacket(flag.closestPlayer.getName().getFormattedText());
            }
            sendToAllLooking(packet);
        }
        if(shouldUpdateFlag) {
            updateFlag();
        }
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PositionGeneratorContainer(id, player, this);
    }


    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        ServerPlayerEntity player;
        if(flag.closestPlayer == null) {
            player = players.get(0);
        }else {
            player = flag.closestPlayer;
        }
        return new PacketPositionSyncResponse(player.getName().getFormattedText(), getEnergy());
    }


    public void updateFlag() {
        flag.setX(new Random().nextInt(150)-75 + getPos().getX());
        flag.setY(new Random().nextInt(150)-75 + getPos().getY());
        flag.setZ(new Random().nextInt(150)-75 + getPos().getZ());
        flag.setY(Math.max(flag.getY(), 1));
        flag.setY(Math.min(flag.getY(), 255));
        flag.players.removeAll(flag.players);
        flag.players.addAll(players);
    }

    @Override
    public void openInventory(PlayerEntity player) {
        openInventory(player);
        if(world.isRemote) return;
        if(!flag.players.contains(player)) {
            flag.players.add((ServerPlayerEntity)player);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("flagX", flag.getX());
        compound.putInt("flagY", flag.getY());
        compound.putInt("flagZ", flag.getZ());

        return compound;
    }
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        flag.setX(compound.getInt("flagX"));
        flag.setY(compound.getInt("flagY"));
        flag.setZ(compound.getInt("flagZ"));
    }
}
