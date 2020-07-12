package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketAnswer;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketGiveQuestion;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class QuestionGeneratorScreen extends GeneratorScreen<QuestionGeneratorContainer> {

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;

    public QuestionGeneratorScreen(QuestionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        addButton(new HintButton());
    }

    @Override
    public void render(int mouseX, int mouseY, final float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        QuestionGeneratorTileEntity questionGeneratorTileEntity = (QuestionGeneratorTileEntity) container.getTileEntity();
        if(questionGeneratorTileEntity.getQuestionLevel() == 10) {
            drawPartRelativeOnScreen(new Point(7,38), new Point(203,0), new Dimension(53,11));
        }else{
            int yoff;
            int xoff;

            if(questionGeneratorTileEntity.getQuestionLevel()%2 == 0) {
                xoff = 7;
                yoff = questionGeneratorTileEntity.getQuestionLevel()/2;
            }else{
                xoff = 33;
                yoff = (questionGeneratorTileEntity.getQuestionLevel()-1)/2;
            }
            yoff = 88 - yoff*10;
            drawPartRelativeOnScreen(new Point(xoff,yoff), new Point(229,10), new Dimension(27,11));
        }


    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString("500", 9, 90 , WHITE);
        font.drawString("1000", 35, 90 , WHITE);
        font.drawString("2500", 9, 80 , WHITE);
        font.drawString("5000", 35, 80 , WHITE);
        font.drawString("10k", 9, 70 , WHITE);
        font.drawString("25k",  35, 70 , WHITE);
        font.drawString("50k",  9, 60 , WHITE);
        font.drawString("100k", 35, 60 , WHITE);
        font.drawString("250k", 9, 50 , WHITE);
        font.drawString("500k",  35, 50 , WHITE);
        font.drawString("1000000", 9, 40 , WHITE);

        font.drawString("50:50", 9,17,WHITE);

        QuestionGeneratorTileEntity questionGeneratorTileEntity = (QuestionGeneratorTileEntity) container.getTileEntity();
        if(questionGeneratorTileEntity.getTipsAvailable()> 1) {
            font.drawString("x" + questionGeneratorTileEntity.getTipsAvailable(), 36,17, DEFAULT_COLOR);
        }
        if(questionGeneratorTileEntity.getTipsAvailable() == 0) {
            drawPartRelativeOnScreen(new Point(7,15), new Point(227,21), new Dimension(29,11));
        }
    }


    private class HintButton extends ImageButton {

        public HintButton() {
            super(RELATIVE_SCREEN_POSITION.x+7, RELATIVE_SCREEN_POSITION.y+15, 29, 11, 0, 0, 11, new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question/hint.png"),  new HintButtonHandler());
            setMessage("50:50");
        }
    }
    private class HintButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            NetworkUtil.INSTANCE.sendToServer(new PacketAnswer(minecraft.player.dimension, container.getTileEntity().getPos(),0));
        }
    }
}
