package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;

import com.agnor99.crazygenerators.container.PongGeneratorContainer;
import com.agnor99.crazygenerators.objects.tile.PongGeneratorTileEntity;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class PongGeneratorScreen extends GeneratorScreen<PongGeneratorContainer> {


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
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        PongGeneratorTileEntity pgte = (PongGeneratorTileEntity) container.getTileEntity();

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
