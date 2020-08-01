package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;

import com.agnor99.crazygenerators.container.PongGeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.pong_generator.PacketPongKeys;
import com.agnor99.crazygenerators.objects.other.generator.pong.DrawObject;
import com.agnor99.crazygenerators.objects.tile.PongGeneratorTileEntity;
import com.agnor99.crazygenerators.util.KeyboardHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class PongGeneratorScreen extends GeneratorScreen<PongGeneratorContainer>{


    public PongGeneratorScreen(PongGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.pong_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/pong_generator.png"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        PongGeneratorTileEntity pgte = (PongGeneratorTileEntity) container.getTileEntity();
        updatePlayerMovement();


        List<DrawObject> gameDrawings = pgte.game.createDrawObjects();
        for(DrawObject drawObject: gameDrawings){
            if(drawObject.drawPosition.x < 0 || drawObject.drawPosition.y < 0) continue;
            drawObject.drawPosition.translate(8,31);
            drawPartRelativeOnScreen(drawObject.drawPosition, drawObject.texturePosition, drawObject.size);
        }
        drawSpeed();
        font.drawString(pgte.game.player.points + ":" + pgte.game.computer.points,64+RELATIVE_SCREEN_POSITION.x,17+RELATIVE_SCREEN_POSITION.y, WHITE);
    }
    private void drawSpeed(){
        int height = calcHeight(56,(int)((PongGeneratorTileEntity)container.getTileEntity()).game.balls[0].getSpeed()*1000,5*1000);
        drawPartRelativeOnScreen(new Point(138,91 -height), new Point(206,39), new Dimension(9,3));

    }
    private void updatePlayerMovement() {
        NetworkUtil.INSTANCE.sendToServer(
                new PacketPongKeys(KeyboardHelper.isHoldingUp(),
                        KeyboardHelper.isHoldingDown(),
                        KeyboardHelper.isHoldingSpace(),
                        container.getTileEntity().getPos(),
                        minecraft.player.dimension)
        );

    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);
        drawHoverMessages(relativeMousePosition);
    }

    @Override
    void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);

    }
}
