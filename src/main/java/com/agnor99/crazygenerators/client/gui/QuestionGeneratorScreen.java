package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.objects.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketAnswer;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketHintRequest;
import com.agnor99.crazygenerators.objects.tile.QuestionGeneratorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
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

    public static final int TIMER_HEIGHT = 56;

    HintButton hint;
    AnswerButton[] answers = new AnswerButton[4];
    public String[] displayAnswers = new String[4];
    String displayQuestion = "";

    boolean hintUsed = false;
    boolean questionAnswered = false;


    int answerSentTick = Integer.MAX_VALUE;


    public QuestionGeneratorScreen(QuestionGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.question_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question_generator.png"));

        for(int i = 0; i < 4; i++) {
            displayAnswers[i] = "";
        }

        this.xSize = 176;
        this.ySize = 188;
    }

    @Override
    protected void init() {
        super.init();
        answers[0] = new AnswerButton(new Point(7,67));
        answers[1] = new AnswerButton(new Point(73,67));
        answers[2] = new AnswerButton(new Point(7,83));
        answers[3] = new AnswerButton(new Point(73,83));
        hint = new HintButton();

        addButton(hint);
        addButton(answers[0]);
        addButton(answers[1]);
        addButton(answers[2]);
        addButton(answers[3]);

        //PlayerInventoryTitlePos
        field_238744_r_ = 7;
        field_238745_s_ = 96;
    }

    void reloadButtons() {
        QuestionGeneratorTileEntity te = (QuestionGeneratorTileEntity) container.getTileEntity();
        if(te.questionGeneratedTime+QuestionGeneratorTileEntity.ANSWER_DELAY < te.getTick()) {
            for (int i = 0; i < 4; i++) {
                answers[i].setAnswer(displayAnswers[i]);
                answers[i].active = !answers[i].answer.equals("");
            }
        }else {
            for (int i = 0; i < 4; i++) {
                answers[i].setAnswer("");
                answers[i].active = false;
            }
        }
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        super.func_230450_a_(stack, partialTicks, mouseX, mouseY);

        updateAnimationVars(stack);


    }

    public void updateQuestion(String question, String[] answers) {
        displayQuestion = question;
        for(int i = 0; i < 4; i++) {
            displayAnswers[i] = answers[i];
        }
    }
    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        super.func_230451_b_(stack, mouseX, mouseY);

        QuestionGeneratorTileEntity questionGeneratorTileEntity = (QuestionGeneratorTileEntity) container.getTileEntity();

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;

        drawButtonText(stack,"50:50", hint);

        font.drawString(stack,"x" + questionGeneratorTileEntity.getTipsAvailable(), 38,17, DEFAULT_COLOR);


        font.drawString(stack, String.valueOf(questionGeneratorTileEntity.getCurrentQuestionPrice()), 83, 17 , WHITE);

        String translatedQuestion = new TranslationTextComponent(displayQuestion).getString();
        List<String> translatedQuestionLines = breakStringIntoLineList(translatedQuestion,123);
        for(int i = 0; i < translatedQuestionLines.size() && i < 3; i++) {
            font.drawString(stack, translatedQuestionLines.get(i), 9, 32 + 10*i, WHITE);
        }


        reloadButtons();

        for(int i = 0; i < 4; i++) {
            drawButtonText(stack, new TranslationTextComponent(answers[i].answer).getString(), answers[i]);
        }


        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);

        drawHoverMessages(stack, relativeMousePosition);
    }


    private void updateAnimationVars(MatrixStack stack) {
        QuestionGeneratorTileEntity qgte = (QuestionGeneratorTileEntity) container.getTileEntity();
        int heightDifference = 0;
        Point colorBarPoint = new Point(0,0);

        if(qgte.questionGeneratedTime+QuestionGeneratorTileEntity.ANSWER_DELAY > qgte.getTick()) {//Show answer
            heightDifference = calcHeight(TIMER_HEIGHT, qgte.getTick() - qgte.questionGeneratedTime, QuestionGeneratorTileEntity.ANSWER_DELAY);
            colorBarPoint = new Point(209,37);
        }else {//show question
            heightDifference = calcHeight(TIMER_HEIGHT, qgte.getTick() - qgte.questionGeneratedTime - QuestionGeneratorTileEntity.ANSWER_DELAY, QuestionGeneratorTileEntity.TIME_PER_QUESTION-QuestionGeneratorTileEntity.ANSWER_DELAY);
            if(heightDifference < TIMER_HEIGHT/2) {
                colorBarPoint = new Point(197,37);
            }else if(heightDifference < TIMER_HEIGHT * 0.75f) {
                colorBarPoint = new Point(203,37);
            }else {
                colorBarPoint = new Point(209,37);
            }
        }

        drawPartRelativeOnScreen(stack, new Point(140,93-heightDifference), colorBarPoint, new Dimension(5,heightDifference));

    }
    private class HintButton extends ImageButton {

        public HintButton() {
            super(RELATIVE_SCREEN_POSITION.x+7, RELATIVE_SCREEN_POSITION.y+15, 29, 11, 0, 0, 11, new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/question/hint.png"),  new HintButtonHandler());
        }
    }
    private class HintButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            if(!hintUsed) {
                NetworkUtil.INSTANCE.sendToServer(new PacketHintRequest(container.getTileEntity().getPos()));
            }
            hint.active = false;
            hintUsed = true;
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
            hintUsed = false;
            hint.active = true;

            questionAnswered = true;
            answers[0].active = false;
            answers[1].active = false;
            answers[2].active = false;
            answers[3].active = false;
            answerSentTick = container.getTicks();
            NetworkUtil.INSTANCE.sendToServer(new PacketAnswer(container.getTileEntity().getPos(),answerButton.answer));
        }
    }
}
