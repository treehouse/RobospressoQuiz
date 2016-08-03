package com.teamtreehouse.robospressoquizapp;

import java.util.Random;

public class QuestionsandAnswers {
    public static final int MAX_NUM = 90;
    public static final int MAX_WRONG_NUM_OFF_BY = 21;

    private int mFirstNum;
    private int mSecondNum;
    private int mAnswer;
    private int mWrongAnsOne;
    private int mWrongAnsTwo;
    private Random mRandomGenerator;

    public QuestionsandAnswers() {
        mRandomGenerator = new Random();
        setNums();
    }

    public void setNums() {
        boolean isSameNum = true;
        boolean isEqualToAns = true;
        mFirstNum = mRandomGenerator.nextInt(MAX_NUM) + 10;
        mSecondNum = mRandomGenerator.nextInt(MAX_NUM) + 10;
        mAnswer = mFirstNum + mSecondNum;
        do {
            mWrongAnsOne = mAnswer + mRandomGenerator.nextInt(MAX_WRONG_NUM_OFF_BY) - 10;
            if (mWrongAnsOne != mAnswer) {
                isEqualToAns = false;
            }
        } while (isEqualToAns);

        do {
            isEqualToAns = true;
            isSameNum = true;
            mWrongAnsTwo = mAnswer + mRandomGenerator.nextInt(MAX_WRONG_NUM_OFF_BY) - 5;
            if (mWrongAnsOne != mWrongAnsTwo) {
                isSameNum = false;
            }
            if (mWrongAnsTwo != mAnswer) {
                isEqualToAns = false;
            }
        } while (isSameNum || isEqualToAns);
    }

    public String getQuestion() {
        String question = mFirstNum + " + " + mSecondNum +" = ?";
        return question;
    }

    public String getAnswer() {
        return String.valueOf(mAnswer);
    }

    public String getWrongAnsOne() {
        return String.valueOf(mWrongAnsOne);
    }

    public String getWrongAnsTwo() {
        return String.valueOf(mWrongAnsTwo);
    }
}
