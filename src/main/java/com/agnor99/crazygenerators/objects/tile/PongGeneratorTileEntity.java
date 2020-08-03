package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.objects.container.PongGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketPongSyncResponse;
import com.agnor99.crazygenerators.objects.other.generator.pong.Board;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class PongGeneratorTileEntity extends GeneratorTileEntity{
    public Board game = new Board(this);

    public PongGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);

    }
    public PongGeneratorTileEntity() {
        this(TileInit.PONG_GENERATOR.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.pong_generator");
    }

    @Override
    public void tick() {
        if(world.isRemote()) return;
        super.tick();
        if(players.size() == 1) {
            game.tick();
        }

    }

    @Override
    public void openInventory(PlayerEntity player) {
        super.openInventory(player);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        super.closeInventory(player);
        game.stop();
    }

   public void addBounceEnergy(float speed) {
        addEnergy((int)speed*800);
    }
    public void addPointEnergy(float speed) {
        addEnergy((int)speed*5000);
    }
    public void addWinEnergy() {
        addEnergy(200000);
    }
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PongGeneratorContainer(id, player, this);
    }


    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        if(players.size() > 1) {
            return new PacketPongSyncResponse(getEnergy(), true);
        }
        game.hardReset();
        game.start();
        return new PacketPongSyncResponse(getEnergy(),false);
    }
}
