package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String LOG_TAG = "main activity";
    private static final String QUESTION_INDEX = "index";
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Boolean mIsCheater;
    private static final int REQUEST_CODE_CHEAT = 0;



    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "in onCreate");
        setContentView(R.layout.activity_quiz);

        mTrueButton       = (Button) findViewById(R.id.true_button);
        mFalseButton      = (Button) findViewById(R.id.false_button);
        mCheatButton      = (Button) findViewById(R.id.cheat_button);
        mNextButton       = (ImageButton) findViewById(R.id.next_button);
        mPrevButton       = (ImageButton) findViewById(R.id.prev_button);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(QUESTION_INDEX, 0);
        }

        setQuestionTextView();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                startActivityForResult(CheatActivity.newIntent(QuizActivity.this, isAnswerTrue), REQUEST_CODE_CHEAT);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousQuestion();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "in onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "in onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "in onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "in onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "in onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "in onSaveInstanceState");
        outState.putInt(QUESTION_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void goToPreviousQuestion() {
        mIsCheater = false;
        int nextIndex = mCurrentIndex - 1;
        if (nextIndex < 0){
            mCurrentIndex = mQuestionBank.length - 1;
        } else {
            mCurrentIndex = nextIndex;
        }
        setQuestionTextView();
    }

    private void goToNextQuestion() {
        mIsCheater = false;
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        setQuestionTextView();
    }

    private void setQuestionTextView() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(Boolean userPressedTrue) {
        int messageResId = 0;
        if (mIsCheater){
            messageResId = R.string.cheat_toast;
        } else {
            if (userPressedTrue == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
                messageResId = R.string.toast_correct;
            } else {
                messageResId = R.string.toast_incorrect;
            }
        }
        Toast.makeText(QuizActivity.this, messageResId , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
