package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.redstone_generator.UpdateSequencePacket;
import com.agnor99.crazygenerators.objects.container.RedstoneGeneratorContainer;
import com.agnor99.crazygenerators.objects.tile.RedstoneGeneratorTileEntity;
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
public class RedstoneGeneratorScreen extends GeneratorScreen<RedstoneGeneratorContainer> {
    SequenceButton button;
    public RedstoneGeneratorScreen(RedstoneGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.redstone_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/redstone_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        button = new SequenceButton();
        addButton(button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        int targetRedstoneData0 = ((RedstoneGeneratorTileEntity)getContainer().getTileEntity()).targetRedstoneData0;
        int targetRedstoneData1 = ((RedstoneGeneratorTileEntity)getContainer().getTileEntity()).targetRedstoneData1;

        drawRedstoneStrength(targetRedstoneData0,31);
        drawRedstoneStrength(targetRedstoneData1,61);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        drawButtonText(new TranslationTextComponent("button.redstone_generator.new").getFormattedText(), button);

        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawHoverMessages(relativeMousePosition);
    }

    private void drawRedstoneStrength(int redstoneStrength, int yoffset) {

        for(int i = 0; i < redstoneStrength; i++) {
            int xoffset;
            xoffset = (i * 8 + (i / 3) * 1 + 8);
            drawPartRelativeOnScreen(new Point(xoffset, yoffset), new Point(198,37), new Dimension(8,24));
        }
    }

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
        drawSequencesHover(mousePosition);
        drawWarningHover(mousePosition);
    }

    private void drawSequencesHover(Point mousePosition) {
        drawSequenceHover(mousePosition,0);
        drawSequenceHover(mousePosition,1);
    }
    private void drawSequenceHover(Point mousePosition, int seqNumber) {
        Point drawPosition = new Point(7,seqNumber == 0 ? 30 : 60);
        Dimension textureSize = new Dimension(126,26);
        RedstoneGeneratorTileEntity rgte = ((RedstoneGeneratorTileEntity)container.getTileEntity());
        if(isMouseOverHoverArea(mousePosition, drawPosition, textureSize)) {
            TranslationTextComponent comp;
            if(rgte.targetRedstoneData0 == 0){
                comp = new TranslationTextComponent("hover.redstone_generator.no_sequence");
                drawHoverMessage(mousePosition, comp.getFormattedText());
            }else {
                comp = new TranslationTextComponent("hover.redstone_generator.sequence");
                drawHoverMessage(mousePosition,
                        comp.getFormattedText().replace(
                                "{State}",
                                seqNumber == 0 ? new TranslationTextComponent("hover.redstone_generator.first").getFormattedText()
                                               : new TranslationTextComponent("hover.redstone_generator.second").getFormattedText()).
                                replace("{Expected}",
                                        seqNumber == 0 ? String.valueOf(rgte.targetRedstoneData0)
                                                       : String.valueOf(rgte.targetRedstoneData1)
                                        )
                                .replace("{Current}", String.valueOf(rgte.lastRedstoneData)));
            }
        }
    }
    private void drawWarningHover(Point mousePosition) {
        Point drawPosition = new Point(137,30);
        Dimension textureSize = new Dimension(12,12);

        if(isMouseOverHoverArea(mousePosition, drawPosition, textureSize)) {
            TranslationTextComponent information = new TranslationTextComponent("hover.redstone_generator.warning");
            drawHoverMessage(mousePosition, information.getFormattedText());
        }
    }

    private class SequenceButton extends ImageButton{
        private SequenceButton() {
            super(RELATIVE_SCREEN_POSITION.x + 7, RELATIVE_SCREEN_POSITION.y + 15, 126, 11, 0, 0, 11, new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/redstone/sequence.png"), new SequenceButtonHandler());
        }
    }
    private class SequenceButtonHandler implements net.minecraft.client.gui.widget.button.Button.IPressable {

        @Override
        public void onPress(Button button) {

            NetworkUtil.INSTANCE.sendToServer(new UpdateSequencePacket(container.getTileEntity().getPos()));

        }
    }
}
