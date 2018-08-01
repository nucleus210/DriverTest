package com.example.android.drivertest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddQuizActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUIRED_IMAGE_SIZE = 400;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView quizImageView;
    private EditText addQuizQuestion;
    private EditText addRightAnswer;
    private EditText addRightAnswerSec;
    private EditText addChoiceA;
    private EditText addChoiceB;
    private EditText addChoiceC;
    private EditText addChoiceD;
    private String mAddQuestion;
    private String mAddCorrectAnswer;
    private String mAddCorrectAnswerSec;
    private String mAddChoiceA;
    private String mAddChoiceB;
    private String mAddChoiceC;
    private String mAddChoiceD;
    private DataBaseHelper db;
    private Bitmap thumbnail;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        startService(intent);

        addQuizQuestion = findViewById(R.id.edit_question_text);
        addRightAnswerSec = findViewById(R.id.right_answer_text_sec);
        addRightAnswer = findViewById(R.id.right_answer_text);
        addChoiceA = findViewById(R.id.edit_choice_a_text);
        addChoiceB = findViewById(R.id.edit_choice_b_text);
        addChoiceC = findViewById(R.id.edit_choice_c_text);
        addChoiceD = findViewById(R.id.edit_choice_d_text);
        quizImageView = findViewById(R.id.quiz_image_view);
        Button myExitToMain = findViewById(R.id.exit_quiz__activity_button);
        myExitToMain.setOnClickListener(this);
        Button myAddToDB = findViewById(R.id.add_quiz_button);
        myAddToDB.setOnClickListener(this);
        Button myAddImage = findViewById(R.id.set_image_button);
        myAddImage.setOnClickListener(this);
        db = new DataBaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_quiz_button:

                // Check user input data  -- > if an image is selected -->
                // correct answer with selected user option
                if (thumbnail == null) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_image, Toast.LENGTH_LONG).show();
                } else if (addQuizQuestion.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_empty_edit_text, Toast.LENGTH_LONG).show();
                } else if (addRightAnswer.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_add_correct_answer, Toast.LENGTH_LONG).show();
                } else if (addChoiceA.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_add_correct_answer,
                            Toast.LENGTH_LONG).show();
                } else if (addChoiceB.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_add_correct_answer,
                            Toast.LENGTH_LONG).show();
                } else if (addChoiceC.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.error_add_correct_answer,
                            Toast.LENGTH_LONG).show();

                } else {
                    addQuizQuestion();
                }
                break;

            case R.id.set_image_button:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);

            case R.id.exit_quiz__activity_button:
                startSecondActivity(); // Go back to mainActivity
                break;

            case R.id.settings_button:
                addQuizQuestion(); // Go to AddQuestion activity. Here we can add new data to database
                break;
        }
    }

    /**
     * Method is used to get picked image, convert it to byte array and  set it to ImageView
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        thumbnail = decodeUri(selectedImage);
                        quizImageView.setImageBitmap(thumbnail);

                    }
                }
        }
    }

    /**
     * Convert and resize our image to 400dp for faster uploading our images to DB
     */
    private Bitmap decodeUri(Uri selectedImage) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < AddQuizActivity.REQUIRED_IMAGE_SIZE
                        || height_tmp / 2 < AddQuizActivity.REQUIRED_IMAGE_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Go back button to LauncherActivity. Creating and intent object.
     */
    private void startSecondActivity() {
        Intent settingsIntent = new Intent(this, LauncherActivity.class);
        startActivity(settingsIntent);
    }

    /**
     * Method is used to convert Bitmap to byte array
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] quizImage(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    /**
     * Method is used to get user input data from views
     */
    private void getValues() {
        mAddQuestion = addQuizQuestion.getText().toString();
        mAddCorrectAnswer = addRightAnswer.getText().toString();
        mAddCorrectAnswerSec = addRightAnswerSec.getText().toString();
        image = quizImage(thumbnail);
        mAddChoiceA = addChoiceA.getText().toString();
        mAddChoiceB = addChoiceB.getText().toString();
        mAddChoiceC = addChoiceC.getText().toString();
        mAddChoiceD = addChoiceD.getText().toString();
    }

    /**
     * Method is used to add new data to database
     */
    private void addQuizQuestion() {
        getValues();

        if (mAddCorrectAnswer.equals(mAddChoiceA)
                || mAddCorrectAnswer.equals(mAddChoiceB)
                || mAddCorrectAnswer.equals(mAddChoiceC)) {
            db.addQuestion(
                    new QuizQuestions(
                    image,
                    mAddQuestion,
                    mAddCorrectAnswerSec,
                            mAddCorrectAnswer,
                            mAddChoiceA,
                            mAddChoiceB,
                            mAddChoiceC,
                            mAddChoiceD));
            Toast.makeText(getBaseContext(), R.string.add_new_row_database_message,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.error_add_correct_answer_database,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method is used to handle device configuration changes
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

}

