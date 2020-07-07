package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.container.GeneratorContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;


public abstract class GeneratorScreen<SpecContainer extends GeneratorContainer> extends ContainerScreen<SpecContainer> {
    protected static ResourceLocation BACKGROUND_TEXTURE;
    Point RELATIVE_SCREEN_POSITION;
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        RELATIVE_SCREEN_POSITION = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);
        blit(RELATIVE_SCREEN_POSITION.x, RELATIVE_SCREEN_POSITION.y, 0 , 0, xSize, ySize);
        drawEnergy();
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

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int DEFAULT_COLOR = 4210752;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        font.drawString(this.title.getFormattedText(),8.0f, 6.0f, DEFAULT_COLOR);
        font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0f, 90.0f, DEFAULT_COLOR);
        font.drawString("Energy: " + container.getEnergy(), 8.0f, 30.0f, DEFAULT_COLOR);
    }
    private void drawPartRelativeOnScreen(Point positionRelativeToScreen, Point positiononTexture, Dimension size) {
        blit(RELATIVE_SCREEN_POSITION.x+positionRelativeToScreen.x, RELATIVE_SCREEN_POSITION.y+positionRelativeToScreen.y, positiononTexture.x, positiononTexture.y, size.width, size.height);
    }
}
