package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketAnswer;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.List;
import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class QuestionGeneratorScreen extends GeneratorScreen<QuestionGeneratorContainer> {

    AnswerButton answer0;
    AnswerButton answer1;
    AnswerButton answer2;
    AnswerButton answer3;

    public QuestionGeneratorScreen(QuestionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question_generator.png"));

    }

    @Override
    protected void init() {
        super.init();
        answer0 = new AnswerButton(new Point(7,67));
        answer1 = new AnswerButton(new Point(73,67));
        answer2 = new AnswerButton(new Point(7,83));
        answer3 = new AnswerButton(new Point(73,83));

        addButton(new HintButton());
        addButton(answer0);
        addButton(answer1);
        addButton(answer2);
        addButton(answer3);

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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        QuestionGeneratorTileEntity questionGeneratorTileEntity = (QuestionGeneratorTileEntity) container.getTileEntity();

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        font.drawString("50:50", 9,17,WHITE);

        if(questionGeneratorTileEntity.getTipsAvailable()> 1) {
            font.drawString("x" + questionGeneratorTileEntity.getTipsAvailable(), 38,17, DEFAULT_COLOR);
        }

        font.drawString(String.valueOf(questionGeneratorTileEntity.getCurrentQuestionPrice()), 83, 17 , WHITE);

        String translated_question = new TranslationTextComponent(questionGeneratorTileEntity.displayQuestion).getFormattedText();
        List<String> translated_question_lines = breakStringIntoLineList(translated_question,132);
        for(int i = 0; i < translated_question_lines.size() && i < 3; i++) {
            font.drawString(translated_question_lines.get(i), 9, 32 + 10*i, WHITE);
        }
        answer0.setAnswer(questionGeneratorTileEntity.displayAnswer0);
        answer1.setAnswer(questionGeneratorTileEntity.displayAnswer1);
        answer2.setAnswer(questionGeneratorTileEntity.displayAnswer2);
        answer3.setAnswer(questionGeneratorTileEntity.displayAnswer3);

        font.drawString(new TranslationTextComponent(questionGeneratorTileEntity.displayAnswer0).getFormattedText(),9,69,WHITE);
        font.drawString(new TranslationTextComponent(questionGeneratorTileEntity.displayAnswer1).getFormattedText(),75,69,WHITE);
        font.drawString(new TranslationTextComponent(questionGeneratorTileEntity.displayAnswer2).getFormattedText(),9,85,WHITE);
        font.drawString(new TranslationTextComponent(questionGeneratorTileEntity.displayAnswer3).getFormattedText(),75,85,WHITE);
    }


    private class HintButton extends ImageButton {

        public HintButton() {
            super(RELATIVE_SCREEN_POSITION.x+7, RELATIVE_SCREEN_POSITION.y+15, 29, 11, 0, 0, 11, new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question/hint.png"),  new HintButtonHandler());
        }
    }
    private class HintButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            NetworkUtil.INSTANCE.sendToServer(new PacketAnswer(minecraft.player.dimension, container.getTileEntity().getPos(),"0"));
        }
    }
    private class AnswerButton extends ImageButton {
        String answer;
        public AnswerButton(Point pos) {
            super(RELATIVE_SCREEN_POSITION.x+pos.x, RELATIVE_SCREEN_POSITION.y+pos.y, 61, 11, 0, 0, 11, new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question/answer.png"),  new AnswerButtonHandler());
            answer = "";
        }
        private void setAnswer(String answer) {
            this.answer = answer;
        }
    }
    private class AnswerButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            AnswerButton answerButton = (AnswerButton) button;
            NetworkUtil.INSTANCE.sendToServer(new PacketAnswer(minecraft.player.dimension, container.getTileEntity().getPos(),answerButton.answer));
        }
    }
}
