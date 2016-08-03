package com.teamtreehouse.robospressoquizapp;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // This is a copy of Sue Lim's submitted project which can be found here
    // https://github.com/slim0926/AddItUp/tree/93563059bc1c59f75049f9280a9536928b60754a

    private static final int NUM_CHOICES = 3;
    private static int sCorrectAnsCount;
    private static int sTotalQuesCount;
    private BGColors mColors = new BGColors();
    private SoundPool mSoundPool;
    private int mRightAnsSound;
    private int mWrongAnsSound;
    private String mStrMessage;
    // Declare our View variables
    private TextView mQuesTextView, mProgressTextView;
    private Button mSubmitButton;
    private RadioGroup mAnsRadioGroup;
    private RadioButton mAnsOneRadio, mAnsTwoRadio, mAnsThreeRadio;
    private RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_it_up);

        // Assign the Views from the layout file to the corresponding variables
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mQuesTextView = (TextView) findViewById(R.id.quesTextView);
        mProgressTextView = (TextView) findViewById(R.id.progressTextView);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mAnsRadioGroup = (RadioGroup) findViewById(R.id.ansRadioGroup);
        mAnsOneRadio = (RadioButton) findViewById(R.id.ansOneRadio);
        mAnsTwoRadio = (RadioButton) findViewById((R.id.ansTwoRadio));
        mAnsThreeRadio = (RadioButton) findViewById(R.id.ansThreeRadio);

        final QuestionsandAnswers qAndA = new QuestionsandAnswers();
        mQuesTextView.setText(qAndA.getQuestion());
        mAnsOneRadio.setText(qAndA.getAnswer());
        mAnsTwoRadio.setText((qAndA.getWrongAnsOne()));
        mAnsThreeRadio.setText(qAndA.getWrongAnsTwo());

        sCorrectAnsCount = 0;
        sTotalQuesCount = 0;

        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mRightAnsSound = mSoundPool.load(MainActivity.this, R.raw.rightanswer, 1);
        mWrongAnsSound = mSoundPool.load(MainActivity.this, R.raw.wronganswer, 1);

        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View v) {

                int selectedId = mAnsRadioGroup.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.ansOneRadio:
                        isRightAnswer(mAnsOneRadio.getText(), qAndA.getAnswer());
                        break;
                    case R.id.ansTwoRadio:
                        isRightAnswer(mAnsTwoRadio.getText(), qAndA.getAnswer());
                        break;
                    case R.id.ansThreeRadio:
                        isRightAnswer(mAnsThreeRadio.getText(), qAndA.getAnswer());
                        break;
                    default:
                        mStrMessage = "You have made an invalid choice";
                        displayToast(mStrMessage);

                }

                sTotalQuesCount++;
                mProgressTextView.setText("Correct: " + sCorrectAnsCount + " / Total: " + sTotalQuesCount);

                mAnsRadioGroup.clearCheck();
                qAndA.setNums();
                mQuesTextView.setText(qAndA.getQuestion());
                Random randomGenerator = new Random();
                int slotNum = randomGenerator.nextInt(NUM_CHOICES) + 1;
                switch (slotNum) {
                    case 1:
                        mAnsOneRadio.setText(qAndA.getAnswer());
                        mAnsTwoRadio.setText((qAndA.getWrongAnsOne()));
                        mAnsThreeRadio.setText(qAndA.getWrongAnsTwo());
                        break;
                    case 2:
                        mAnsOneRadio.setText((qAndA.getWrongAnsOne()));
                        mAnsTwoRadio.setText(qAndA.getAnswer());
                        mAnsThreeRadio.setText(qAndA.getWrongAnsTwo());
                        break;
                    case 3:
                        mAnsOneRadio.setText(qAndA.getWrongAnsTwo());
                        mAnsTwoRadio.setText((qAndA.getWrongAnsOne()));
                        mAnsThreeRadio.setText(qAndA.getAnswer());
                        break;
                    default:
                        System.out.println("There was an error.");
                }
                int color = mColors.getColor();
                mRelativeLayout.setBackgroundColor(color);
                mSubmitButton.setTextColor(color);

            }

        };
        mSubmitButton.setOnClickListener(listener);
    }

    private void isRightAnswer(CharSequence choice, String answer) {
        if (choice.equals(answer)) {
            mStrMessage = "Correct!";
            displayToast(mStrMessage);
            mSoundPool.play(mRightAnsSound, 1.0f, 1.0f, 0, 0, 1.0f);
            sCorrectAnsCount++;
        } else {
            mStrMessage = "Incorrect.";
            displayToast(mStrMessage);
            mSoundPool.play(mWrongAnsSound, 1.0f, 1.0f, 0, 0, 1.5f);
        }
    }

    private void displayToast(String strMessage) {
        Toast toast;
        toast = Toast.makeText(MainActivity.this, strMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }
}
