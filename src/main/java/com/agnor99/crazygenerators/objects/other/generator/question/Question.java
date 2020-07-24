package com.agnor99.crazygenerators.objects.other.generator.question;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum  Question {

    q0(0,0),
    q1(1,1),
    q2(2,2),
    q3(3,3),
    q4(4,4),
    q5(5,5),
    q6(6,6),
    q7(7,7),
    q8(8,8),
    q9(9,9),
    q10(10,10),
    q11(11,6),
    q12(12, 10),
    q13(13,3),
    q14(14,9);
    private final String question;
    private final String answer;
    private final String fake1;
    private final String fake2;
    private final String fake3;
    private final int questionTier;

    Question(int id, int questionTier) {
        String prefix = "question.question" + id + ".";
        this.question = prefix + "question";
        this.answer = prefix + "answer0";
        this.fake1 = prefix + "answer1";
        this.fake2 = prefix + "answer2";
        this.fake3 = prefix + "answer3";
        this.questionTier = questionTier;
    }
    public boolean isCorrectAnswer(String answer) {
        return this.answer.equals(answer);
    }
    public static List<Question> getQuestionsForTier(int tier) {
        Question[] allQuestions = values();
        List<Question> questionsInTier = new ArrayList<>();
        for(Question question: allQuestions) {
            if(question.questionTier == tier) {
                questionsInTier.add(question);
            }
        }
        return questionsInTier;
    }
    public static Question getQuestionInTier(int tier) {
        List<Question> questions = getQuestionsForTier(tier);
        return questions.get(new Random().nextInt(questions.size()));
    }
    public String[] getAnswerPossibilities() {
        List<String> answerPossibilities = new ArrayList<>();
        answerPossibilities.add(answer);
        answerPossibilities.add(fake1);
        answerPossibilities.add(fake2);
        answerPossibilities.add(fake3);
        answerPossibilities.sort((o1, o2) -> new Random().nextInt(3)-1);
        answerPossibilities.sort((o1, o2) -> new Random().nextInt(3)-1);
        String[] retValue = new String[answerPossibilities.size()]; {
            for(int i = 0; i < answerPossibilities.size(); i++) {
                retValue[i] = answerPossibilities.get(i);
            }
        }
        return retValue;
    }

    public String[] getWrongAnswers() {
        List<String> wrongAnswers = new ArrayList<String>();
        wrongAnswers.add(fake1);
        wrongAnswers.add(fake2);
        wrongAnswers.add(fake3);
        String[] retValues = new String[2];
        retValues[0] = wrongAnswers.get(new Random().nextInt(3));
        wrongAnswers.remove(retValues[0]);
        retValues[1] = wrongAnswers.get(new Random().nextInt(2));
        return retValues;
    }

    public String getQuestion() {
        return question;
    }
}
