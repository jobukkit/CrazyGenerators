package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;

import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.objects.container.TimingGeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.timing_generator.PacketButtonPress;
import com.agnor99.crazygenerators.objects.other.generator.timing.ButtonData;
import com.agnor99.crazygenerators.objects.tile.TimingGeneratorTileEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class TimingGeneratorScreen extends GeneratorScreen<TimingGeneratorContainer> {

    public int lastDelay = Integer.MIN_VALUE;
    public int lastEnergyAdded = Integer.MIN_VALUE;

    public static final int TIMER_HEIGHT =  56;
    ClickButton clickButton;

    public TimingGeneratorScreen(TimingGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.timing_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/timing_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        clickButton = new ClickButton(new Point(7,66));
        addButton(clickButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        TimingGeneratorTileEntity tgte = (TimingGeneratorTileEntity) container.getTileEntity();


        if(lastDelay >= 0 && lastDelay <= TimingGeneratorTileEntity.TICKS_TO_CLICK) {
            int height = calcHeight(TIMER_HEIGHT, lastDelay, TimingGeneratorTileEntity.TICKS_TO_CLICK);
            drawPartRelativeOnScreen(new Point(138,91 -height), new Point(206,39), new Dimension(9,3));
        }
        int tickToUnlock = tgte.getTickToUnlock();

        boolean isButtonVisible = tgte.getTick() >= tickToUnlock
                && tgte.getTick() <= tickToUnlock + TimingGeneratorTileEntity.TICKS_TO_CLICK;
        clickButton.updatePosition(tgte.buttonPosition, isButtonVisible);


    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString(((TimingGeneratorTileEntity)container.getTileEntity()).getMultiplier() + "x", 9,17,WHITE);
        if(lastDelay != Integer.MIN_VALUE) {
            font.drawString(lastDelay/20.0d + "s", 28, 17, WHITE);
        }
        if(lastEnergyAdded != Integer.MIN_VALUE) {
            font.drawString("+" + lastEnergyAdded + " RF", 74, 17, WHITE);
        }


        drawButtonText(new TranslationTextComponent("button.timing_generator.click").getFormattedText(), clickButton);


        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);
        drawHoverMessages(relativeMousePosition);
    }

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
        drawMultiplierHover(mousePosition);
        drawDelayHover(mousePosition);
        drawEnergyAddedHover(mousePosition);

    }
    private void drawMultiplierHover(Point relativeMousePosition) {
        Point boxPoint = new Point(7,15);
        Dimension boxSize = new Dimension(15,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.timing_generator.multiplier").getFormattedText());
        }
    }
    private void drawDelayHover(Point relativeMousePosition) {
        Point boxPoint = new Point(26,15);
        Dimension boxSize = new Dimension(42,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.timing_generator.delay").getFormattedText());
        }
    }
    private void drawEnergyAddedHover(Point relativeMousePosition) {
        Point boxPoint = new Point(72,15);
        Dimension boxSize = new Dimension(62,11);

        if(isMouseOverHoverArea(relativeMousePosition, boxPoint, boxSize)) {
            drawHoverMessage(relativeMousePosition, new TranslationTextComponent("hover.timing_generator.energy_added").getFormattedText());
        }
    }
    private class ClickButton extends ImageButton {

        public ClickButton(Point pos) {
            super(RELATIVE_SCREEN_POSITION.x+pos.x, RELATIVE_SCREEN_POSITION.y+pos.y,
                    ButtonData.buttonDimension.width, ButtonData.buttonDimension.height,
                    0, 0, ButtonData.buttonDimension.height,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/timing/click.png"),
                    new ClickButtonHandler());

        }
        void updatePosition(Point p, boolean visible) {
            this.x = RELATIVE_SCREEN_POSITION.x+p.x;
            this.y = RELATIVE_SCREEN_POSITION.y+p.y;
            this.visible = visible;
            this.active = visible;
        }
    }
    private class ClickButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {

            NetworkUtil.INSTANCE.sendToServer(new PacketButtonPress(minecraft.player.dimension, container.getTileEntity().getPos(), container.getTicks()));


        }
    }
}
