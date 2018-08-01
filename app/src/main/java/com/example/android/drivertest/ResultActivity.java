package com.example.android.drivertest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultActivity extends Activity {
    private ListView lv;
    private int mScore;
    private String mUserName;
    private String[] myCorrect;
    private String[] mWrongAnswers;
    private boolean myAdapterCheck = false;

    @SuppressWarnings("unused")
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        lv = findViewById(R.id.list);
        RatingBar bar = findViewById(R.id.ratingBar);
        final Button viewList = findViewById(R.id.list_view_button);
        final Button backToMain = findViewById(R.id.back_to_main_activity);
        final Button composeMail = findViewById(R.id.compose_email_button);
        final CheckBox mShowCorrect = findViewById(R.id.show_correct_answers);
        final CheckBox mShowWrong = findViewById(R.id.show_wrong_answers);
        final TextView testResult = findViewById(R.id.scores_text_view);
        final Animator mButtonAnimation = AnimatorInflater
                .loadAnimator(ResultActivity.this, R.animator.button_click_anim);

        bar.setNumStars(8);
        bar.setStepSize(1.2f);
        // get intents from MainActivity and fetch data to ResultActivity

        Bundle b = getIntent().getExtras();
        assert b != null;
        mScore = b.getInt("myScore");
        int mCountRows = b.getInt("countRows");
        mUserName = b.getString("userName");
        ArrayList<String> mWrongAnswersId = getIntent()
                .getStringArrayListExtra("wrongAnswers");
        final ArrayList<String> mCorrectAnswersId = getIntent()
                .getStringArrayListExtra("correctAnswers");
        myCorrect = listToArray((mCorrectAnswersId));   //convert ArrayList to String array
        mWrongAnswers = listToArray(mWrongAnswersId);   //convert ArrayList to String array
        System.out.println(Arrays.toString(myCorrect)); //prints element
        int result = calcPercentage(mCountRows, mScore); // calculate percentage correct answers

        //display score
        bar.setRating(result / 10); //result in percentage divided by 10 to get switch numbers for bar Rating

        switch (result / 10) {  //result in percentage divided by 10 to get switch numbers for switch cases

            case 1:
                testResult.setText(String.format("%s%d", getString(R.string.score_a), mScore));
                break;
            case 2:
                testResult.setText(String.format("%s%d", getString(R.string.score_a), mScore));
                break;
            case 3:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_cc)));
                break;
            case 4:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_dd)));
                break;
            case 5:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ee)));
                break;
            case 6:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ee)));
                break;
            case 7:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ee)));
                break;
            case 8:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ee)));
                break;
            case 9:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ee)));
                break;
            case 10:
                testResult.setText(String.format("%s%d%s", getString(R.string.score_a),
                        mScore, getString(R.string.score_ff)));
                break;
        }
        // Buttons onClickListeners

        composeMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonAnimation.setTarget(composeMail);
                mButtonAnimation.start();
                submitTestResult();
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonAnimation.setTarget(backToMain);
                mButtonAnimation.start();
                backToMainActivity(); // back to main
            }
        });

        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonAnimation.setTarget(viewList);
                mButtonAnimation.start();
                if (mShowWrong.isChecked()) {
                    mShowWrong.setChecked(false);
                } else if (mShowCorrect.isChecked()) {
                    mShowCorrect.setChecked(false);
                }
                ShowRecords(); // show all questions to ListView
            }
        });

        mShowCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAdapterCheck) {
                    myAdapterCheck = false;
                    lv.setAdapter(null);
                }
                if (!mShowCorrect.isChecked()) {
                    lv.setAdapter(null);
                } else if (mShowWrong.isChecked()) {
                    lv.setAdapter(null);
                    mShowWrong.setChecked(false);
                    showCorrectAnswers(myCorrect);
                } else {
                    showCorrectAnswers(myCorrect); // show user wrong answers
                }
            }
        });

        mShowWrong.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (myAdapterCheck) {
                    myAdapterCheck = false;
                    lv.setAdapter(null);
                }
                if (!mShowWrong.isChecked()) {
                    lv.setAdapter(null);
                } else if (mShowCorrect.isChecked()) {
                    lv.setAdapter(null);
                    mShowCorrect.setChecked(false);
                    showWrongAnswers(mWrongAnswers); // show user wrong answers
                } else {
                    showWrongAnswers(mWrongAnswers); // show user wrong answers
                }
            }
        });
    }

    /**
     * This method is used to convert ArrayList to String array.
     */
    private static <T> String[] listToArray(List<T> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = list.get(i).toString();
        return array;
    }

    /**
     * This method is used to submit user result to via mail
     */
    private void submitTestResult() {
        int totalScores = mScore;
        String name = mUserName;
        String submitData = testResultSummary(totalScores, name);
        composeEmail(submitData);
    }

    /**
     * This method is used to display result summary test results
     */
    private String testResultSummary(int number, String name) {
        String viewTestResults = getString(R.string.hi) + getString(R.string.tab);
        if (mUserName.isEmpty()) {
            viewTestResults += getString(R.string.dot);
            viewTestResults += getString(R.string.new_line);
            viewTestResults += getString(R.string.result_message_a);
            viewTestResults += getString(R.string.new_line) + getString(R.string.result_message_b);
            viewTestResults += number;
            viewTestResults += getString(R.string.scores);
        } else {
            viewTestResults += getString(R.string.tab) + name;
            viewTestResults += getString(R.string.new_line);
            viewTestResults += getString(R.string.result_message_a);
            viewTestResults += getString(R.string.new_line) + getString(R.string.result_message_b);
            viewTestResults += number;
            viewTestResults += getString(R.string.scores);
        }
        return viewTestResults;
    }

    /**
     * This method calculate percentage of correct user answers
     */
    private int calcPercentage(int countRow, int mScore) {
        int total;
        total = (mScore * 100) / countRow;
        return total;
    }

    /**
     * This method is used to show all user correct answers in list view when checkbox
     * is checked or hide on un check.
     */
    private void showCorrectAnswers(String[] correctAnswers) {
        DataBaseHelper db = new DataBaseHelper(this);
        final ArrayList<QuizQuestions> question = new ArrayList<>(db.getRowById(correctAnswers));
        QuizAdapter mQuizAdapter = new QuizAdapter(this, question);
        lv.setAdapter(mQuizAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(question.get(position)),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to show all user wrong answers in list view when checkbox
     * is checked or hide on un check.
     */
    private void showWrongAnswers(String[] wrongAnswers) {
        DataBaseHelper db = new DataBaseHelper(this);
        final ArrayList<QuizQuestions> question = new ArrayList<>(db.getRowById(wrongAnswers));
        QuizAdapter mQuizAdapter = new QuizAdapter(this, question);
        lv.setAdapter(mQuizAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(question.get(position)),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to show all questions in list view.
     */
    private void ShowRecords() {
        DataBaseHelper db = new DataBaseHelper(this);
        final ArrayList<QuizQuestions> question = new ArrayList<>(db.getAllRows());
        QuizAdapter mQuizAdapter = new QuizAdapter(this, question);

        if (!myAdapterCheck) {
            myAdapterCheck = true;
            lv.setAdapter(mQuizAdapter);
        } else {
            lv.setAdapter(null);
            myAdapterCheck = false;
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(question.get(position)),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method send user test details to email.
     */
    private void composeEmail(String scoreResult) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Driver Test results");
        intent.putExtra(Intent.EXTRA_TEXT, scoreResult);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Back to MainActivity
     */
    private void backToMainActivity() {
        Intent settingsIntent = new Intent(this, MainActivity.class);
        startActivity(settingsIntent);
    }

    /**
     * This method handle device rotation.
     */
    @SuppressWarnings("unused")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

}