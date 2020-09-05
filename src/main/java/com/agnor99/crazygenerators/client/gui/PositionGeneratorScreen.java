package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.position_generator.*;
import com.agnor99.crazygenerators.objects.container.PositionGeneratorContainer;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class PositionGeneratorScreen extends GeneratorScreen<PositionGeneratorContainer> {

    PositionGeneratorTileEntity pgte;
    Button newCoordsButton;
    Button chatButton;

    public PositionGeneratorScreen(PositionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.position_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/position_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        newCoordsButton = new NewCoordsButton();
        addButton(newCoordsButton);
        chatButton = new ChatButton();
        addButton(chatButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        pgte = (PositionGeneratorTileEntity) container.getTileEntity();

    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString("x: " + pgte.flag.getX(),9,17, WHITE);
        font.drawString("y: " + pgte.flag.getY(),9,32, WHITE);
        font.drawString("z: " + pgte.flag.getZ(),9,47, WHITE);
        if(!StringUtils.isEmpty(pgte.flag.playerName)) {
            font.drawString(pgte.flag.playerName,55, 17, WHITE);
        }else {
            font.drawString(new TranslationTextComponent("text.position_generator.player_not_set").getFormattedText(),55, 17, WHITE);
        }

        font.drawString(new TranslationTextComponent("text.position_generator.distance").getFormattedText() +
                ((PositionGeneratorTileEntity)container.getTileEntity()).flag.getSmallestDistance()
                ,55,31, WHITE);

        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawButtonText(new TranslationTextComponent("button.position_generator.position").getFormattedText(), newCoordsButton);
        drawButtonText(new TranslationTextComponent("button.position_generator.chat").getFormattedText(), chatButton);

        drawHoverMessages(relativeMousePosition);
    }

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
        drawXHover(mousePosition);
        drawYHover(mousePosition);
        drawZHover(mousePosition);
        drawDistanceHover(mousePosition);
        drawPlayerHover(mousePosition);
    }
    private void drawXHover(Point relativeMousePosition) {
        Point boxPoint = new Point(7,15);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getFormattedText().replace("{Axis}", "X"));
        }
    }
    private void drawYHover(Point relativeMousePosition) {
        Point boxPoint = new Point(7,30);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getFormattedText().replace("{Axis}", "Y"));
        }
    }
    private void drawZHover(Point relativeMousePosition) {
        Point boxPoint = new Point(7,45);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getFormattedText().replace("{Axis}", "Z"));
        }
    }
    private void drawDistanceHover(Point relativeMousePosition) {
        Point boxPoint = new Point(53,30);
        Dimension boxSize = new Dimension(80,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.position_generator.distance").getFormattedText().replace("{Player}", pgte.flag.playerName==null ? "{Player}" : pgte.flag.playerName));
        }
    }
    private void drawPlayerHover(Point relativeMousePosition) {
        Point boxPoint = new Point(53,15);
        Dimension boxSize = new Dimension(80,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.position_generator.player").getFormattedText());
        }
    }
    private class NewCoordsButton extends ImageButton {
        public NewCoordsButton() {
            super(RELATIVE_SCREEN_POSITION.x+7, RELATIVE_SCREEN_POSITION.y+60,
                    132, 11,
                    0, 0, 11,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/position/coords.png"),
                    new NewCoordsHandler());

        }
    }

    private class NewCoordsHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            NetworkUtil.INSTANCE.sendToServer(new NewCoordsPacket(minecraft.player.dimension, container.getTileEntity().getPos()));
        }
    }
    private class ChatButton extends ImageButton {
        public ChatButton() {
            super(RELATIVE_SCREEN_POSITION.x+7, RELATIVE_SCREEN_POSITION.y+75,
                    132, 11,
                    0, 0, 11,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/position/coords.png"),
                    new ChatHandler());

        }
    }

    private class ChatHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("X:" + pgte.flag.getX() + "  Y:" + pgte.flag.getY() + "  Z:" + pgte.flag.getZ()));
        }
    }
}
