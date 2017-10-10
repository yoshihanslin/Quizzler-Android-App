package com.londonappbrewery.quizzler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    // TODO: Declare member variables here:
    Button mTrueButton;
    Button mFalseButton;
    TextView mQuestionTextView;
    TextView mScoreTextView;
    ProgressBar mProgressBar;
    int mIndex; //=0 if uninitialized
    int mScore;
    int mQuestion; // question ID from R.string

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_1, true),
            new TrueFalse(R.string.question_2, true),
            new TrueFalse(R.string.question_3, true),
            new TrueFalse(R.string.question_4, true),
            new TrueFalse(R.string.question_5, true),
            new TrueFalse(R.string.question_6, false),
            new TrueFalse(R.string.question_7, true),
            new TrueFalse(R.string.question_8, false),
            new TrueFalse(R.string.question_9, true),
            new TrueFalse(R.string.question_10, true),
            new TrueFalse(R.string.question_11, false),
            new TrueFalse(R.string.question_12, false),
            new TrueFalse(R.string.question_13,true)
    };

    // TODO: Declare constants here
    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0/mQuestionBank.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //savedInstanceState==null when first started app
//        savedInstanceState!=null after screen rotate
//          and called onSaveInstanceState(Bundle outState) to save state values in outState
        if(savedInstanceState!=null){
            mScore = savedInstanceState.getInt("ScoreKey");
            mIndex = savedInstanceState.getInt("IndexKey");
        }
        else{
            mScore = 0;
            mIndex= 0;
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mQuestionTextView =(TextView) findViewById(R.id.question_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mQuestion = mQuestionBank[mIndex].getQuestionID(); //mIndex=0
        mQuestionTextView.setText(mQuestion);
        mScoreTextView.setText("Score: "+mScore+"/"+mQuestionBank.length);

        mTrueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "True pressed!",Toast.LENGTH_SHORT).show();
                checkAnswer(true);
                updateQuestion();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "False pressed!",Toast.LENGTH_SHORT).show();
                checkAnswer(false);
                updateQuestion();
            }
        });
    }

    private void updateQuestion(){
        mIndex = (mIndex+1)% mQuestionBank.length; // (1 to 12 to 0) = (1 to 13)% 13
        if (mIndex ==0){
//            AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
            //OR
            AlertDialog.Builder alert = new AlertDialog.Builder(this); // this refers to MainActivity
            alert.setTitle("Game Over");
            alert.setCancelable(false); //if user can click on somewhere outside alert box to exit box
            alert.setMessage("You scored "+mScore+" points.");
            alert.setPositiveButton("Close Application", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }
        mQuestion = mQuestionBank[mIndex].getQuestionID();
        mQuestionTextView.setText(mQuestion);
        mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        mScoreTextView.setText("Score: "+mScore+"/"+mQuestionBank.length);
    }
    private void checkAnswer (boolean userSelection){
        boolean correctAnswer = mQuestionBank[mIndex].isAnswer();
        if(userSelection==correctAnswer){
            Toast.makeText(getApplicationContext(),R.string.correct_toast,Toast.LENGTH_SHORT).show();
            mScore = mScore + 1;
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.incorrect_toast,Toast.LENGTH_SHORT).show();

        }
    }

    //when there's a screen rotation, system detects onSaeInstanceState() method,
    // and restart by going from onDestroy() --> onCreate()
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //store info in Bundle outState
        outState.putInt("ScoreKey", mScore);
        outState.putInt("IndexKey", mIndex);
    }
}
