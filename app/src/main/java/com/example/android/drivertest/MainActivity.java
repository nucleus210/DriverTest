package com.example.android.drivertest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int qId;                                    //variable for holding id number
    private int myScore;                                //scores counter
    private int countRow;                               //variable for holding total rows in table
    private int remainQuiz;                             //variable for remaining questions
    private int totalQuiz;                              //variable for total questions
    private String mUserName;                           //variable for user name
    private long mCountDownTime;                        //time for quiz
    private long mTimeLeftInMillis;                     //time left count  down timer
    private QuizQuestions currentQ;
    private ImageView pic;
    private TextView txtQuestion;
    private TextView setTextCount;
    private ProgressBar mProgressBar;
    private LinearLayout myRoot;
    private View checkBoxQuestion;
    private View inputTextQuestion;
    private View radioButtonQuestion;
    private String[] secondaryAnswersIds;
    private List<QuizQuestions> mQuizList;
    private final ArrayList<String> mWrongIds = new ArrayList<>();
    private final ArrayList<String> mCorrectIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper dba = new DataBaseHelper(this);
        //open database
        dba.createDataBase();
        dba.openDataBase();
        mQuizList = dba.getAllRows(); //call getAllRows method and put data from database into list
        countRow = dba.getQuizCount() - 1;
        secondaryAnswersIds = dba.getSecondaryAnswerIds();
        dba.close();
        //call count method
        mCountDownTime = countRow * 20 * 1000; // set 1 minutes time for quiz in milliseconds

        // Check populated list from database cursor is empty.
        if (mQuizList != null && mQuizList.size() != 0) {
            currentQ = mQuizList.get(qId);
        }

// Define views

        ImageButton backToLauncher = findViewById(R.id.back_to_launch);
        backToLauncher.setOnClickListener(this);
        ImageButton nextQuizButton = findViewById(R.id.next_quiz_button);
        nextQuizButton.setOnClickListener(this);
        Button resultActivity = findViewById(R.id.start_result_activity);
        resultActivity.setOnClickListener(this);

        pic = findViewById(R.id.quiz_image_view);
        txtQuestion = findViewById(R.id.text_question);
        myRoot = findViewById(R.id.main_question_view);
        setTextCount = findViewById(R.id.exam_title_text);
        mProgressBar = findViewById(R.id.time_progress_bar);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inputTextQuestion = inflater.inflate(R.layout.input_question, myRoot, false);
        checkBoxQuestion = inflater.inflate(R.layout.check_box_questions, myRoot, false);
        radioButtonQuestion = inflater.inflate(R.layout.radio_group_questions, myRoot, false);

        displayQuizCount();         // call method to populate Views from database
        // radioGroup.clearCheck();    //un check all radio buttons
        setQuestionView();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mUserName = b.getString("userName");
        }

        /*
          Define and start count down timer.
         */

        mProgressBar.setMax((int) (mCountDownTime / 10));
        mProgressBar.setProgress((int) (mCountDownTime / 10));
        CountDownTimer mCountDownTimer = new CountDownTimer(mCountDownTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mProgressBar.setProgress((int) (mCountDownTime / 10 - millisUntilFinished / 10));
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getBaseContext(), R.string.times_up, Toast.LENGTH_SHORT).show();
                createDialogMessage();
            }
        };

        /*
          Start add to database activity.
         */

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar,
                "progress", mProgressBar.getProgress(), mProgressBar.getMax());
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mCountDownTimer.start(); // start timer
    }

    /**
     * Start add to database activity.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_to_launch:               // back to launcher activity
                ImageButton backToLauncher = findViewById(R.id.back_to_launch);
                final Animator mButtonAnimationA = AnimatorInflater
                        .loadAnimator(MainActivity.this, R.animator.button_click_anim);
                mButtonAnimationA.setTarget(backToLauncher);
                mButtonAnimationA.start();
                backToLauncher();
                break;

            case R.id.start_result_activity:        // start Result Activity
                final EditText mInputAnswer = findViewById(R.id.input_question_test);
                String inputRightAnswer = getString(R.string.input_quiz_right_answer);

                if (mInputAnswer.getText().toString().length() == 0) {
                    Toast.makeText(getBaseContext(), R.string.view_result, Toast.LENGTH_LONG).show();
                    return;
                } else if (mInputAnswer.getText().toString().contentEquals(inputRightAnswer)) {
                    myScore++; //increment scores
                    Toast.makeText(getBaseContext(), R.string.correct, Toast.LENGTH_SHORT).show();
                    final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                    mCorrectIds.add(str); // When user give correct answer add new record to String Array
                } else {
                    final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                    mWrongIds.add(str); // When user give wrong answer add new record to String Array
                }
                Button resultActivity = findViewById(R.id.start_result_activity);
                final Animator mButtonAnimation = AnimatorInflater
                        .loadAnimator(MainActivity.this, R.animator.button_click_anim);
                mButtonAnimation.setTarget(resultActivity);
                mButtonAnimation.start();
                goToResultActivity();

                break;

            case R.id.next_quiz_button:             // next quiz
                ImageButton nextQuizButton = findViewById(R.id.next_quiz_button);
                final Animator mButtonAnimationB = AnimatorInflater
                        .loadAnimator(MainActivity.this, R.animator.button_click_anim);
                mButtonAnimationB.setTarget(nextQuizButton);
                mButtonAnimationB.start();
                startResultActivity();
                break;
        }
    }

    /**
     * Method is used for starting Launcher Activity
     */

    private void backToLauncher() {
        Intent intent = new Intent(MainActivity.this, LauncherActivity.class);
        startActivity(intent);
    }

    /**
     * Method is used for starting dialog message after count down timer is finnish.
     */

    private void createDialogMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time's UP");
        builder.setMessage("Do you wan't to try again?");

        builder.setPositiveButton("Again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                recreate();      // start activity again
                dialog.cancel(); // close dialog message
            }
        });

        builder.setNegativeButton("Results", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                goToResultActivity();// start result activity
                dialog.cancel();         // close dialog message

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method is used for starting Result Activity. When count down timer is finish start
     * dialog message and user have to choice to continue to Result Activity or Try Again.
     * Also is used when result button is clicked.
     */

    private void goToResultActivity() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("myScore", myScore);
        b.putInt("countRows", countRow); // Intent user scores
        b.putString("userName", mUserName); // send user name data
        b.putStringArrayList("correctAnswers", mCorrectIds); // Intent user correct answers
        b.putStringArrayList("wrongAnswers", mWrongIds); // Intent user wrong answers
        intent.putExtras(b);
        startActivity(intent); // start Result activity
    }

    /**
     * Method is used for starting Result Activity.
     */

    private void startResultActivity() {
        String[] integerStrings = secondaryAnswersIds;
        // Splits each spaced integer into a String array.
        int[] integers = new int[integerStrings.length];
        // Creates the integer array.
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.parseInt(integerStrings[i]);
        }

        int secQuestion = qId;
        if (secQuestion == integers[0]) {
            // count current position in table and set new view on finish show results

            final CheckBox mOptA = findViewById(R.id.check_box_a);
            final CheckBox mOptB = findViewById(R.id.check_box_b);
            final CheckBox mOptC = findViewById(R.id.check_box_c);
            final CheckBox mOptD = findViewById(R.id.check_box_d);
            ArrayList<CheckBox> mChecks = new ArrayList<>();

            if (mOptA.isChecked() && currentQ.get_answer().contentEquals(mOptA.getText())
                    || currentQ.get_answerSec().contentEquals(mOptA.getText())) {
                mChecks.add(mOptA);
            }

            if (mOptB.isChecked() && currentQ.get_answer().contentEquals(mOptB.getText())
                    || currentQ.get_answerSec().contentEquals(mOptB.getText())) {
                mChecks.add(mOptB);
            }

            if (mOptC.isChecked() && currentQ.get_answer().contentEquals(mOptC.getText())
                    || currentQ.get_answerSec().contentEquals(mOptC.getText())) {
                mChecks.add(mOptC);
            }

            if (mOptD.isChecked() && currentQ.get_answer().contentEquals(mOptD.getText())
                    || currentQ.get_answerSec().contentEquals(mOptD.getText())) {
                mChecks.add(mOptD);
            }

            if (!mOptA.isChecked() && !mOptB.isChecked() && !mOptC.isChecked() && !mOptD.isChecked()) {
                Toast.makeText(getBaseContext(), R.string.please_make_your_choice,
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                int arraySize = mChecks.size();
                if (arraySize == 2) {
                    final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                    mWrongIds.add(str); // When user give wrong answer add new record to String Array
                    Toast.makeText(getBaseContext(), R.string.correct, Toast.LENGTH_SHORT).show();
                    displayQuizCount();
                    myRoot.removeView(checkBoxQuestion);

                } else {
                    final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                    mCorrectIds.add(str);
                    displayQuizCount();
                    myRoot.removeView(checkBoxQuestion);
                }
            }

        } else {
            RadioGroup radioGroup1 = findViewById(R.id.quiz_radio_group);

            //Check if one of radio buttons in radio group is checked.
            if (radioButtonQuestion.getParent() != null) {
                RadioButton answer = findViewById(radioGroup1.getCheckedRadioButtonId());

                //Check if one of radio buttons in radio group is checked.
                if (radioGroup1.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getBaseContext(), R.string.please_make_your_choice,
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Check given answer is equals to correct answer
                    if (currentQ.get_answer().contentEquals(answer.getText())) {
                        myScore++; //increment scores
                        Toast.makeText(getBaseContext(), R.string.correct, Toast.LENGTH_SHORT).show();
                        final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                        mCorrectIds.add(str); // When user give correct answer add new record to String Array
                        radioGroup1.clearCheck();
                    } else {
                        final String str = Integer.toString(currentQ.get_ID()); // convert int to string
                        mWrongIds.add(str); // When user give wrong answer add new record to String Array
                        radioGroup1.clearCheck();
                    }
                    displayQuizCount();
                }
            }
        }

        int result = qId + 1;
        RadioGroup radioGroup1 = findViewById(R.id.quiz_radio_group);
        if (qId < countRow && result != integers[0] && radioGroup1 != null) {
            radioGroup1.clearCheck();
            setQuestionView();

        } else if (result == integers[0]) {
            setMultipleQuestions();

        } else if (qId < countRow) {
            setQuestionView();
        } else if (qId == totalQuiz) {
            Toast.makeText(getBaseContext(), R.string.view_result, Toast.LENGTH_SHORT).show();
        } else if (qId == countRow) {
            setLastQuestion();
            qId++;
        }
    }

    /**
     * Set Quiz Image and text views.
     */


    private void setQuestionView() {
        currentQ = mQuizList.get(qId);
        txtQuestion.setText(currentQ.get_question());
        pic.setImageBitmap(convertToBitmap(currentQ.get_image()));
        myRoot.removeView(radioButtonQuestion);
        myRoot.addView(radioButtonQuestion, myRoot.getChildCount() - 1);

        RadioButton mGetAnswerA = findViewById(R.id.answer_radio_button_a);
        RadioButton mGetAnswerB = findViewById(R.id.answer_radio_button_b);
        RadioButton mGetAnswerC = findViewById(R.id.answer_radio_button_c);
        mGetAnswerA.setText(currentQ.get_choiceA());
        mGetAnswerB.setText(currentQ.get_choiceB());
        mGetAnswerC.setText(currentQ.get_choiceC());
        qId++;
        animateQuestionsView();
    }

    /**
     * Set Quiz Image and text views.
     */

    private void setMultipleQuestions() {
        currentQ = mQuizList.get(qId);
        txtQuestion.setText(currentQ.get_question());
        pic.setImageBitmap(convertToBitmap(currentQ.get_image()));
        myRoot.removeView(radioButtonQuestion);
        myRoot.addView(checkBoxQuestion, myRoot.getChildCount() - 1);
        final CheckBox mGetAnswerAA = findViewById(R.id.check_box_a);
        final CheckBox mGetAnswerBB = findViewById(R.id.check_box_b);
        final CheckBox mGetAnswerCC = findViewById(R.id.check_box_c);
        final CheckBox mGetAnswerDD = findViewById(R.id.check_box_d);

        mGetAnswerAA.setText(currentQ.get_choiceA());
        mGetAnswerBB.setText(currentQ.get_choiceB());
        mGetAnswerCC.setText(currentQ.get_choiceC());
        mGetAnswerDD.setText(currentQ.get_choiceD());
        qId++;
        animateQuestionsViewC();
    }

    /**
     * Set Quiz Image and text views.
     */

    private void setLastQuestion() {

        String inputText = getString(R.string.input_question);
        txtQuestion.setText(inputText);
        pic.setImageResource(R.drawable.a742);
        myRoot.removeView(radioButtonQuestion);
        myRoot.addView(inputTextQuestion, myRoot.getChildCount() - 1);
        final EditText mInputAnswer = findViewById(R.id.input_question_test);
        TextView mInputText = findViewById(R.id.input_question_hint_text);
        mInputText.setText(R.string.type_your_answer);
        mInputAnswer.setHint(R.string.type_answer);
        animateQuestionsInput();
    }

    /**
     * Method is used to animate populated data.
     */

    private void animateQuestionsView() {
        final RadioButton mGetAnswerA = findViewById(R.id.answer_radio_button_a);
        final RadioButton mGetAnswerB = findViewById(R.id.answer_radio_button_b);
        final RadioButton mGetAnswerC = findViewById(R.id.answer_radio_button_c);
        pic.setX(1000f);
        txtQuestion.setX(1000f);
        mGetAnswerA.setX(1000f);
        mGetAnswerB.setX(1000f);
        mGetAnswerC.setX(1000f);
        ObjectAnimator picMoveX = ObjectAnimator.ofFloat(pic,
                "translationX", 0);
        picMoveX.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(pic,
                "alpha", 0f, 100f);
        fadeIn.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(picMoveX).with(fadeIn);
        animatorSet.start();  //start animations set

        ObjectAnimator animation1 = ObjectAnimator.ofFloat(txtQuestion,
                "translationX", 0);
        animation1.setDuration(1200);
        animation1.start();
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mGetAnswerA,
                "translationX", 0);
        animation2.setDuration(1400);
        animation2.start();
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(mGetAnswerB,
                "translationX", 0);
        animation3.setDuration(1600);
        animation3.start();
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(mGetAnswerC,
                "translationX", 0);
        animation4.setDuration(1800);
        animation4.start();
    }

    /**
     * Method is used to animate populated data.
     */

    private void animateQuestionsViewC() {
        final CheckBox mGetAnswerAA = findViewById(R.id.check_box_a);
        final CheckBox mGetAnswerBB = findViewById(R.id.check_box_b);
        final CheckBox mGetAnswerCC = findViewById(R.id.check_box_c);
        final CheckBox mGetAnswerDD = findViewById(R.id.check_box_d);
        pic.setX(1000f);
        txtQuestion.setX(1000f);
        mGetAnswerAA.setX(1000f);
        mGetAnswerBB.setX(1000f);
        mGetAnswerCC.setX(1000f);
        mGetAnswerDD.setX(1000f);
        ObjectAnimator picMoveX = ObjectAnimator.ofFloat(pic, "translationX", 0);
        picMoveX.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(pic, "alpha", 0f, 100f);
        fadeIn.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(picMoveX).with(fadeIn);
        animatorSet.start(); //start animations set

        ObjectAnimator animation1 = ObjectAnimator.ofFloat(txtQuestion,
                "translationX", 0);
        animation1.setDuration(1200);
        animation1.start();
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(mGetAnswerAA,
                "translationX", 0);
        animation2.setDuration(1400);
        animation2.start();
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(mGetAnswerBB,
                "translationX", 0);
        animation3.setDuration(1600);
        animation3.start();
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(mGetAnswerCC,
                "translationX", 0);
        animation4.setDuration(1800);
        animation4.start();
        ObjectAnimator animation5 = ObjectAnimator.ofFloat(mGetAnswerDD,
                "translationX", 0);
        animation5.setDuration(2000);
        animation5.start();
    }

    private void animateQuestionsInput() {
        pic.setX(1000f);
        final TextView mInputText = findViewById(R.id.input_question_hint_text);
        final EditText mInputAnswer = findViewById(R.id.input_question_test);
        ObjectAnimator picMoveX = ObjectAnimator.ofFloat(pic,
                "translationX", 0);
        picMoveX.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(pic,
                "alpha", 0f, 100f);
        fadeIn.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(picMoveX).with(fadeIn);
        animatorSet.start(); //start animations set

        ObjectAnimator animationA = ObjectAnimator.ofFloat(txtQuestion,
                "alpha", 0f, 100f);
        animationA.setDuration(1200);
        animationA.start();
        ObjectAnimator animationB = ObjectAnimator.ofFloat(mInputAnswer,
                "alpha", 0f, 100f);
        animationB.setDuration(20000);
        animationB.start();
        ObjectAnimator animationC = ObjectAnimator.ofFloat(mInputText,
                "alpha", 0f, 100f);
        animationC.setDuration(20000);
        animationC.start();
    }

    /**
     * Convert image from byte[] to bitmap.
     */

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * Show list view with all quiz questions.
     */

    private void displayQuizCount() {
        int total = countRow + 1;
        totalQuiz = total;
        remainQuiz = remainQuiz + 1;
        int remain = remainQuiz;

        // Format displayed values
        String quizCounter = String.format(getString(R.string.quiz_counter)
                + getString(R.string.tab)
                + "%d " + getString(R.string.slash)
                + " %d",
                remain,
                total);
        setTextCount.setText(quizCounter); //set string in TextView
    }

    /**
     * Method to handle device configuration changes
     */

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

    /**
     * Convert timer value from millisecond to MM:SS:MM
     */

    private void updateCountDownText() {

        TextView mTimer = findViewById(R.id.time_left);
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60; // To get minutes from milliseconds
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60; // To get remaining seconds

        // Format time to 00:00
        String timeLeftFormatted = String.format(Locale.getDefault(),
                getString(R.string.time_format), minutes, seconds);
        mTimer.setText(timeLeftFormatted); //display formatted string on screen
    }
}



