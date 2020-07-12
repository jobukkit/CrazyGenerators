package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class QuestionGeneratorTileEntity extends GeneratorTileEntity{



    private int tipsAvailable = 3;
    private int questionLevel = 0;
    private final int[] ENERGY_PER_QUESTION = new int[]{500,1000,2500,5000,10000,25000,50000,100000,250000,500000,1000000};
    private Question currentQuestion = new Question();

    public QuestionGeneratorTileEntity(final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
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
    public final void validateAnswer(int answer) {
        if(answer == 0) {
            addEnergy(ENERGY_PER_QUESTION[questionLevel]);
        }
        questionLevel++;
    }
    public int getQuestionLevel() {
        return questionLevel;
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
    private class Question{
        String question;
        String answer1;
        String answer2;
        String answer3;
        String answer4;
        private Question() {
            question = "How many vanilla Trees do exist in 1.15";
            answer1 = "4";
            answer2 = "5";
            answer3 = "6";
            answer4 = "7";
        }
    }
}
