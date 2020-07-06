package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class QuestionGeneratorScreen extends ContainerScreen<QuestionGeneratorContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question_generator.png");

    public QuestionGeneratorScreen(QuestionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 175;
        this.ySize = 183;
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
        Point pointToBlit = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);
        blit(pointToBlit.x, pointToBlit.y, 0 , 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int DEFAULT_COLOR = 4210752;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        font.drawString(this.title.getFormattedText(),8.0f, 6.0f, DEFAULT_COLOR);
        font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0f, 90, DEFAULT_COLOR);
    }
}
