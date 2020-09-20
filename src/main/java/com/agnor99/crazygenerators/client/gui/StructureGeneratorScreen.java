package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureGenerationPacket;
import com.agnor99.crazygenerators.network.packets.structure_generator.StructureGetPacket;
import com.agnor99.crazygenerators.objects.container.StructureGeneratorContainer;
import com.agnor99.crazygenerators.objects.tile.StructureGeneratorTileEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class StructureGeneratorScreen extends GeneratorScreen<StructureGeneratorContainer> {

    StructureButton structureButton;
    StructureBlockButton[] structureBlockButtons = new StructureBlockButton[3];
    StructureGeneratorTileEntity gte;
    public StructureGeneratorScreen(StructureGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.structure_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/structure_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        structureButton = new StructureButton();
        addButton(structureButton);

        for(int i = 0; i < 3; i++) {
            structureBlockButtons[i] = new StructureBlockButton(i+1);
            addButton(structureBlockButtons[i]);
        }

        gte = (StructureGeneratorTileEntity)container.getTileEntity();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                if(gte.structureTarget[x][z] != StructureGeneratorTileEntity.STRUCTURE_NONE) {
                    drawPartRelativeOnScreen(new Point(66 + x*9,31 + z*9), new Point(185 + 10*(gte.structureTarget[x][z]-1),3), new Dimension(9,9));
                }
            }
        }
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;


        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        font.drawString("N", 86,17, WHITE);
        font.drawString("E", 118,50, WHITE);
        font.drawString("S", 86,83, WHITE);
        font.drawString("W", 54,50, WHITE);

        drawButtonText(new TranslationTextComponent("button.structure_generator.new").getFormattedText(), structureButton);
        for(StructureBlockButton button: structureBlockButtons) {
            drawButtonText(new TranslationTextComponent("button.structure_generator.get").getFormattedText(), button);
        }
        drawHoverMessages(relativeMousePosition);
    }

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
    }

    private class StructureButton extends ImageButton {

        public StructureButton() {
            super(RELATIVE_SCREEN_POSITION.x + 7, RELATIVE_SCREEN_POSITION.y + 15,
                    73, 11, 0, 0, 11,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/structure/structure.png"), new StructureButtonHandler());
        }
    }
    private class StructureButtonHandler implements IPressable {

        @Override
        public void onPress(Button button) {
            NetworkUtil.INSTANCE.sendToServer(new StructureGenerationPacket(gte.getPos()));
        }
    }
    private class StructureBlockButton extends ImageButton {
        public final int structureBlock;

        public StructureBlockButton(int blockId) {
            super(RELATIVE_SCREEN_POSITION.x + 7, RELATIVE_SCREEN_POSITION.y + 33 + 15*(blockId-1),
                    41, 11, 0, 0, 11,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/structure/get" + blockId + ".png"), new StructureBlockButtonHandler());
            structureBlock = blockId;
        }
    }

    private class StructureBlockButtonHandler implements IPressable {

        @Override
        public void onPress(Button button) {
            StructureBlockButton b = (StructureBlockButton) button;
            NetworkUtil.INSTANCE.sendToServer(new StructureGetPacket(gte.getPos(), b.structureBlock));
        }
    }
}
