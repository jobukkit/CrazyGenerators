package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.container.GeneratorContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;


public abstract class GeneratorScreen<SpecContainer extends GeneratorContainer> extends ContainerScreen<SpecContainer> {
    protected ResourceLocation BACKGROUND_TEXTURE;
    Point RELATIVE_SCREEN_POSITION;
    int hoverFrames = 0;
    String lastHover = "";
    String messageToDraw;
    public GeneratorScreen(SpecContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.ySize = 183;

    }

    protected void setBackgroundTexture(ResourceLocation loc) {
        BACKGROUND_TEXTURE = loc;
    }
    @Override
    public void render(int mouseX, int mouseY, final float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int DEFAULT_COLOR = 4210752;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        font.drawString(this.title.getFormattedText(),8.0f, 6.0f, DEFAULT_COLOR);
        font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0f, 90.0f, DEFAULT_COLOR);
        font.drawString("Energy: " + container.getEnergy(), 8.0f, 30.0f, DEFAULT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        RELATIVE_SCREEN_POSITION = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);
        blit(RELATIVE_SCREEN_POSITION.x, RELATIVE_SCREEN_POSITION.y, 0 , 0, xSize, ySize);
        drawEnergy();
        drawWarnings();
        drawHoverMessages(new Point(mouseX, mouseY));
    }
    private void drawEnergy() {
        Point energyContainerPoint = new Point(152,40);
        Point energyContainerTexturePoint = new Point(180,40);
        Dimension energyContainerSize = new Dimension(16,56);

        int currentEnergyContainerHeight = calcEnergyContainerHeight(energyContainerSize.height);
        int missingHeight = energyContainerSize.height-currentEnergyContainerHeight;

        energyContainerPoint.translate(0,missingHeight);
        energyContainerTexturePoint.translate(0,missingHeight);
        energyContainerSize.setSize(energyContainerSize.width, energyContainerSize.height-missingHeight);

        drawPartRelativeOnScreen(energyContainerPoint, energyContainerTexturePoint, energyContainerSize);
    }
    private int calcEnergyContainerHeight(int maxHeight) {
        return (int)(maxHeight*(((float)container.getEnergy())/((float)container.getCapacity())));
    }
    private void drawWarnings() {
        if(hasWarning()) {
            Point drawPosition = new Point(138,39);
            Point warningTexture = new Point(182,18);
            Dimension textureSize = new Dimension(9,10);

            drawPartRelativeOnScreen(drawPosition,warningTexture,textureSize);
        }
    }

    private boolean hasWarning() {
        if(container.getEnergy() == container.getCapacity()) {
            return true;
        }
        return false;
    }

    void drawHoverMessages(Point mousePosition) {

        Point relativeMousePosition = new Point(mousePosition);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawEnergyHover(relativeMousePosition);
        drawWarningHover(relativeMousePosition);
    }

    private void drawWarningHover(Point relativeMousePosition) {
        if(!hasWarning()) return;
        Point drawPosition = new Point(138,39);
        Dimension textureSize = new Dimension(9,10);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            drawHoverMessage(relativeMousePosition, "Energy is full, but might also be empty, cause to an bug. if it's empty, just try to produce some energy, then it will update! ");
        }
    }

    private void drawEnergyHover(Point relativeMousePosition) {
        Point drawPosition = new Point(151,39);
        Dimension textureSize = new Dimension(18,58);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            drawHoverMessage(relativeMousePosition, container.getEnergy() + "RF");
        }
    }

    private void drawHoverMessage(Point relativeMousePosition, String message) {
        if(lastHover.equals(message)) {
            hoverFrames++;
        }else {
            hoverFrames = 0;
            lastHover = message;
            messageToDraw = message;
        }
        String realMessage = message;
        final int DEFAULT_COLOR = 4210752;
        if(isMessageToLong(message)) {
            drawBox(relativeMousePosition, font.getStringWidth(message.substring(0,20)));
            if(hoverFrames != 0 && hoverFrames%20 == 0) {
                messageToDraw = messageToDraw.substring(1) + messageToDraw.charAt(0);
            }
            realMessage = messageToDraw.substring(0,18);
        }else{
            drawBox(relativeMousePosition, font.getStringWidth(message));
        }

        font.drawString(realMessage, relativeMousePosition.x+RELATIVE_SCREEN_POSITION.x+5, relativeMousePosition.y+RELATIVE_SCREEN_POSITION.y+5, DEFAULT_COLOR);
    }
    boolean isMessageToLong(String message) {
        return message.length() > 18;
    }
    protected void drawBox(Point relativeMousePosition, int textLength){
        Point messageOpenTexturePoint = new Point(195,0);
        Dimension messageOpenTextureDimension = new Dimension(4,17);

        Point messageMiddleTexturePoint = new Point(200,0);
        Dimension messageMiddleTextureDimension = new Dimension(1,17);

        Point messageEndTexturePoint = new Point(202,0);
        Dimension messageEndTextureDimension = new Dimension(4,17);

        drawPartRelativeOnScreen(relativeMousePosition,messageOpenTexturePoint, messageOpenTextureDimension);

        Point movingPoint = new Point(relativeMousePosition);
        movingPoint.translate(messageOpenTextureDimension.width,0);
        for(int i = 0; i <= textLength; i++) {
            drawPartRelativeOnScreen(movingPoint, messageMiddleTexturePoint, messageMiddleTextureDimension);
            movingPoint.translate(1,0);
        }
        drawPartRelativeOnScreen(movingPoint, messageEndTexturePoint, messageEndTextureDimension);
    }

    private boolean isMouseOverHoverArea(Point relativeMouseLocation, Point upperPosition, Dimension size) {
        return new Rectangle(upperPosition,size).contains(relativeMouseLocation);
    }

    private void drawPartRelativeOnScreen(Point positionRelativeToScreen, Point positiononTexture, Dimension size) {
        blit(RELATIVE_SCREEN_POSITION.x+positionRelativeToScreen.x, RELATIVE_SCREEN_POSITION.y+positionRelativeToScreen.y, positiononTexture.x, positiononTexture.y, size.width, size.height);
    }
}
