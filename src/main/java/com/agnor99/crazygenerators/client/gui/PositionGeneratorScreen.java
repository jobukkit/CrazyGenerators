package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.position_generator.*;
import com.agnor99.crazygenerators.objects.container.PositionGeneratorContainer;
import com.agnor99.crazygenerators.objects.tile.PositionGeneratorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.UUID;


@OnlyIn(Dist.CLIENT)
public class PositionGeneratorScreen extends GeneratorScreen<PositionGeneratorContainer> {

    PositionGeneratorTileEntity pgte;
    Button newCoordsButton;
    Button chatButton;

    public PositionGeneratorScreen(PositionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.position_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/position_generator.png"));
        pgte = (PositionGeneratorTileEntity)screenContainer.getTileEntity();
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
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        super.func_230451_b_(stack, mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString(stack, "x: " + pgte.flag.getX(),9,17, WHITE);
        font.drawString(stack,"y: " + pgte.flag.getY(),9,32, WHITE);
        font.drawString(stack,"z: " + pgte.flag.getZ(),9,47, WHITE);
        if(!StringUtils.isEmpty(pgte.flag.playerName)) {
            font.drawString(stack, pgte.flag.playerName,55, 17, WHITE);
        }else {
            font.drawString(stack, new TranslationTextComponent("text.position_generator.player_not_set").getString(),55, 17, WHITE);
        }

        font.drawString(stack,new TranslationTextComponent("text.position_generator.distance").getString() +
                ((PositionGeneratorTileEntity)container.getTileEntity()).flag.getSmallestDistance()
                ,55,31, WHITE);

        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawButtonText(stack, new TranslationTextComponent("button.position_generator.position").getString(), newCoordsButton);
        drawButtonText(stack, new TranslationTextComponent("button.position_generator.chat").getString(), chatButton);

        drawHoverMessages(stack, relativeMousePosition);
    }

    @Override
    protected void drawHoverMessages(MatrixStack stack, Point mousePosition) {
        super.drawHoverMessages(stack, mousePosition);
        drawXHover(stack, mousePosition);
        drawYHover(stack, mousePosition);
        drawZHover(stack, mousePosition);
        drawDistanceHover(stack, mousePosition);
        drawPlayerHover(stack, mousePosition);
    }
    private void drawXHover(MatrixStack stack, Point relativeMousePosition) {
        Point boxPoint = new Point(7,15);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(stack, relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getString().replace("{Axis}", "X"));
        }
    }
    private void drawYHover(MatrixStack stack, Point relativeMousePosition) {
        Point boxPoint = new Point(7,30);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(stack, relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getString().replace("{Axis}", "Y"));
        }
    }
    private void drawZHover(MatrixStack stack, Point relativeMousePosition) {
        Point boxPoint = new Point(7,45);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(stack, relativeMousePosition, new TranslationTextComponent("hover.position_generator.location").getString().replace("{Axis}", "Z"));
        }
    }
    private void drawDistanceHover(MatrixStack stack, Point relativeMousePosition) {
        Point boxPoint = new Point(53,30);
        Dimension boxSize = new Dimension(80,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(stack, relativeMousePosition, new TranslationTextComponent("hover.position_generator.distance").getString().replace("{Player}", pgte.flag.playerName==null ? "{Player}" : pgte.flag.playerName));
        }
    }
    private void drawPlayerHover(MatrixStack stack, Point relativeMousePosition) {
        Point boxPoint = new Point(53,15);
        Dimension boxSize = new Dimension(80,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(stack, relativeMousePosition, new TranslationTextComponent("hover.position_generator.player").getString());
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
            NetworkUtil.INSTANCE.sendToServer(new NewCoordsPacket(container.getTileEntity().getPos()));
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
            if(ModList.get().isLoaded("journeymap")) {
                String str = "[name:\"flag\", x:" + pgte.flag.getX() + ", y:" + pgte.flag.getY() + ", z:" + pgte.flag.getZ()+"]";
                NetworkUtil.INSTANCE.sendToServer(new WayPointChatMessagePacket(str));
            }else {
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("X:" + pgte.flag.getX() + "  Y:" + pgte.flag.getY() + "  Z:" + pgte.flag.getZ()), new UUID(0L, 0L));
            }
        }
    }
}
