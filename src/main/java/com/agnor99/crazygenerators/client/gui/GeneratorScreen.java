package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.container.GeneratorContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class GeneratorScreen<SpecContainer extends GeneratorContainer> extends ContainerScreen<SpecContainer> {
    protected ResourceLocation BACKGROUND_TEXTURE;
    Point RELATIVE_SCREEN_POSITION;
    public GeneratorScreen(SpecContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 184;


    }

    @Override
    protected void init() {
        super.init();
        RELATIVE_SCREEN_POSITION = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

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
        return container.getEnergy() == container.getCapacity();
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
        List<String> lines = breakStringIntoLineList(message);
        if(lines.size() == 1) {
            drawHoverBox(relativeMousePosition, new Dimension(font.getStringWidth(lines.get(0))+9,10*lines.size()+7));
        }else {
            drawHoverBox(relativeMousePosition, new Dimension(100,10*lines.size()+7));
        }
        final int WHITE = 16777215;
        for(int i = 0; i <lines.size(); i++) {
            String line = lines.get(i);
            font.drawString(line, relativeMousePosition.x + RELATIVE_SCREEN_POSITION.x + 5, relativeMousePosition.y + RELATIVE_SCREEN_POSITION.y + 5 + 10*i, WHITE);
        }
    }
    private List<String> breakStringIntoLineList(String message) {
        String[] words = message.split(" ");
        List<String> lines = new ArrayList<>();
        lines.add("");
        for(String word: words) {
            String currLine = lines.get(lines.size()-1);
            currLine = currLine + " " + word;
            currLine = currLine.trim();

            if(font.getStringWidth(currLine) < 92) {
                lines.set(lines.size()-1, currLine);
            }else {
                lines.add(word);
            }
        }
        return lines;
    }
    boolean isMessageToLong(String message) {
        return message.length() > 18;
    }
    protected void drawHoverBox(Point relativeMousePosition, Dimension boxSize){

        drawBoxTop(relativeMousePosition, boxSize.width);
        Point offsettedPoint = new Point(relativeMousePosition);
        offsettedPoint.translate(0,4);
        drawBoxMid(offsettedPoint, boxSize);
        offsettedPoint.translate(0,boxSize.height-8);

        drawBoxBottom(offsettedPoint,boxSize.width);

    }
    void drawBoxTop(Point relativeMousePosition, int width) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,0);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,0);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,0);
        messagePartDimension[2] = new Dimension(4,4);

        drawPartRelativeOnScreen(relativeMousePosition, messagePartPoint[0], messagePartDimension[0]);
        int xoff = 0;
        for(; xoff < width-8; xoff++) {
            drawPartRelativeOnScreen(new Point(relativeMousePosition.x+xoff+4, relativeMousePosition.y), messagePartPoint[1], messagePartDimension[1]);
        }
        drawPartRelativeOnScreen(new Point(relativeMousePosition.x+xoff+4,relativeMousePosition.y),messagePartPoint[2], messagePartDimension[2]);

    }
    void drawBoxMid(Point offsettedMousePosition, Dimension size) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,5);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,5);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,5);
        messagePartDimension[2] = new Dimension(4,4);


        for(int top = 0; top < size.height-9; top++) {
            Point linePoint = new Point(offsettedMousePosition);
            linePoint.translate(0,top);
            drawPartRelativeOnScreen(linePoint, messagePartPoint[0], messagePartDimension[0]);
            int xoff = 0;
            for(; xoff < size.width-8; xoff++) {
                drawPartRelativeOnScreen(new Point(linePoint.x+xoff+4, linePoint.y), messagePartPoint[1], messagePartDimension[1]);
            }

            linePoint.translate(xoff+4,0);
            drawPartRelativeOnScreen(linePoint, messagePartPoint[2], messagePartDimension[2]);
        }
    }
    void drawBoxBottom(Point offsettedMousePosition, int width) {
        Point[] messagePartPoint = new Point[3];
        Dimension[] messagePartDimension = new Dimension[3];

        messagePartPoint[0] = new Point(180,7);
        messagePartDimension[0] = new Dimension(4,4);

        messagePartPoint[1] = new Point(185,7);
        messagePartDimension[1] = new Dimension(1,4);

        messagePartPoint[2] = new Point(187,7);
        messagePartDimension[2] = new Dimension(4,4);

        drawPartRelativeOnScreen(offsettedMousePosition, messagePartPoint[0], messagePartDimension[0]);
        int xoff = 0;
        for(; xoff < width-8; xoff++) {
            drawPartRelativeOnScreen(new Point(offsettedMousePosition.x+xoff+4, offsettedMousePosition.y), messagePartPoint[1], messagePartDimension[1]);
        }
        drawPartRelativeOnScreen(new Point(offsettedMousePosition.x+xoff+4,offsettedMousePosition.y),messagePartPoint[2], messagePartDimension[2]);

    }
    private boolean isMouseOverHoverArea(Point relativeMouseLocation, Point upperPosition, Dimension size) {
        return new Rectangle(upperPosition,size).contains(relativeMouseLocation);
    }

    protected void drawPartRelativeOnScreen(Point positionRelativeToScreen, Point positionTexture, Dimension size) {
        blit(RELATIVE_SCREEN_POSITION.x+positionRelativeToScreen.x, RELATIVE_SCREEN_POSITION.y+positionRelativeToScreen.y, positionTexture.x, positionTexture.y, size.width, size.height);
    }
}
