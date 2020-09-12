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

        boolean[] redstoneData = ((RedstoneGeneratorTileEntity)getContainer().getTileEntity()).targetRedstoneData;
        if(redstoneData != null) {
            for (int i = 0; i < 20; i++) {
                int y;
                int x;
                if (i < 10) {
                    y = 31;
                } else {
                    y = 61;
                }
                x = ((i % 10) * 12 + ((i % 10) / 2) * 1 + 8);

                Point referenceLocation = redstoneData[i] ? new Point(198, 37) : new Point(211, 37);
                Point drawPosition = new Point(x, y);
                Dimension drawDimension = new Dimension(12, 24);
                drawPartRelativeOnScreen(drawPosition, referenceLocation, drawDimension);
            }
        }
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

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
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
