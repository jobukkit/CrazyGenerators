package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;

import com.agnor99.crazygenerators.container.TimingGeneratorContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class TimingGeneratorScreen extends GeneratorScreen<TimingGeneratorContainer> {





    public TimingGeneratorScreen(TimingGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.timing_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/timing_generator.png"));

    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        updateAnimationVars();

        drawHoverMessages(new Point(mouseX, mouseY));
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString("50:50", 9,17,DEFAULT_COLOR);

        }


    private void updateAnimationVars() {

    }
}
