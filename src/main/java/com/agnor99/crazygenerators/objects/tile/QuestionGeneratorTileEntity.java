package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketQuestionSyncResponse;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class QuestionGeneratorTileEntity extends GeneratorTileEntity{

    private Question question;

    public String displayQuestion = "";
    public String displayAnswer0 = "";
    public String displayAnswer1 = "";
    public String displayAnswer2 = "";
    public String displayAnswer3 = "";

    private int tipsAvailable = 3;
    private int questionLevel = 0;
    private final int[] ENERGY_PER_QUESTION = new int[]{500,1000,2500,5000,10000,25000,50000,100000,250000,500000,1000000};

    public QuestionGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        updateQuestion();

    }
    public QuestionGeneratorTileEntity() {
        this(TileInit.QUESTION_GENERATOR.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.question_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new QuestionGeneratorContainer(id, player, this);
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        PacketQuestionSyncResponse syncPacket = new PacketQuestionSyncResponse(question, getEnergy(), pos);
        return syncPacket;
    }

    public boolean validateAnswer(String answer) {
        boolean success = false;
        if(question.isCorrectAnswer(answer)) {
            addEnergy(ENERGY_PER_QUESTION[questionLevel]);
            updateQuestion();
            success = true;
        }else{
            resetQuestion();
        }
        return success;
    }
    public Question getQuestion() {
        return question;
    }
    private void resetQuestion() {
        questionLevel = 0;
        question = Question.getQuestionInTier(questionLevel);
    }
    private void updateQuestion() {
        questionLevel++;
        question = Question.getQuestionInTier(questionLevel);
    }
    public void updateQuestion(String question, String[] answers) {
        displayQuestion = question;
        displayAnswer0 = answers[0];
        displayAnswer1 = answers[1];
        displayAnswer2 = answers[2];
        displayAnswer3 = answers[3];

    }
    public int getQuestionLevel() {
        return questionLevel;
    }
    public int getCurrentQuestionPrice() {
        return ENERGY_PER_QUESTION[questionLevel];
    }

    public void setQuestionLevel(int amount) {
        this.questionLevel = amount;
    }

    public int getTipsAvailable() {
        return tipsAvailable;
    }

    public void setTipsAvailable(int tipsAvailable) {
        this.tipsAvailable = tipsAvailable;
    }
}
