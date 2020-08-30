package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketPositionSyncResponse;
import com.agnor99.crazygenerators.objects.container.PositionGeneratorContainer;
import com.agnor99.crazygenerators.objects.other.generator.position.Flag;
import net.minecraft.block.BarrierBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;


public class PositionGeneratorTileEntity extends GeneratorTileEntity{

    public Flag flag;

    @OnlyIn(Dist.CLIENT)
    private EntityType<SlimeEntity> type = EntityType.SLIME;
    @OnlyIn(Dist.CLIENT)
    private SlimeEntity marker;

    public PositionGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        flag = new Flag(null,0,0,0);
        updateFlag();


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
        if(!world.isRemote()) {
            super.tick();
            if (flag.player != null) {
                BlockPos pos = flag.player.getPosition();
                if (pos.withinDistance(new BlockPos(flag.getX(), flag.getY(), flag.getZ()), 5.0f)) {
                    addEnergy(10000);
                    updateFlag();
                }
            }else{
                updateFlag();
            }
        }
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PositionGeneratorContainer(id, player, this);
    }


    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        if(flag.player == null) {
            flag.player = players.get(0);
        }
        if(players.size() > 1) {
            return new PacketPositionSyncResponse(flag.player.getName().getFormattedText(), getEnergy(), true);
        }
        return new PacketPositionSyncResponse(flag.player.getName().getFormattedText(), getEnergy(),false);
    }


    public void updateFlag() {
        flag.setX(new Random().nextInt(150)-75 + getPos().getX());
        flag.setY(new Random().nextInt(150)-75 + getPos().getY());
        flag.setZ(new Random().nextInt(150)-75 + getPos().getZ());
        flag.setY(Math.max(flag.getY(), 1));
        flag.setY(Math.min(flag.getY(), 255));
    }
}
