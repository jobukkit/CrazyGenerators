package com.agnor99.crazygenerators.objects.tile;

import com.agnor99.crazygenerators.container.QuestionGeneratorContainer;
import com.agnor99.crazygenerators.init.TileInit;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.question_generator.PacketTimeOut;
import com.agnor99.crazygenerators.network.packets.sync.PacketAbstractSyncResponse;
import com.agnor99.crazygenerators.network.packets.sync.PacketQuestionSyncResponse;
import com.agnor99.crazygenerators.objects.other.generator.question.Question;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;

public class QuestionGeneratorTileEntity extends GeneratorTileEntity{

    private Question question;

    public static final int TIME_PER_QUESTION = 600; //30 Sekunden
    public static final int ANSWER_DELAY = 100; // 5 Sekunden

    public int questionGeneratedTime = 0;



    private int tipsAvailable = 3;
    private int questionLevel = 0;
    private final int[] ENERGY_PER_QUESTION = new int[]{500,1000,2500,5000,10000,25000,50000,100000,250000,500000,1000000};

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
    public void tick() {
        if(world.isRemote()) return;
        super.tick();
        if(questionGeneratedTime + TIME_PER_QUESTION == tick) {
            resetQuestion();
            PacketTimeOut packet = new PacketTimeOut(getPos(), question);
            for(ServerPlayerEntity p: players) {

                NetworkUtil.INSTANCE.sendTo(packet, p.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        }

    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new QuestionGeneratorContainer(id, player, this);
    }

    @Override
    public PacketAbstractSyncResponse generateSyncPacket() {
        if(players.size() > 1) {
            return new PacketQuestionSyncResponse(question, getEnergy(), true);
        }
        resetQuestion();
        return new PacketQuestionSyncResponse(question, getEnergy(), false);
    }

    public boolean handleAnswer(String answer) {
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
        setQuestionGeneratedTime(getTick());
        tipsAvailable = 3;
    }
    private void updateQuestion() {
        questionLevel++;
        if(questionLevel >= ENERGY_PER_QUESTION.length) {
            questionLevel = 0;
        }
        question = Question.getQuestionInTier(questionLevel);
        setQuestionGeneratedTime(getTick());
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
    public int getQuestionGeneratedTime() {
        return questionGeneratedTime;
    }

    public void setQuestionGeneratedTime(int questionGeneratedTime) {
        this.questionGeneratedTime = questionGeneratedTime;
    }

}
