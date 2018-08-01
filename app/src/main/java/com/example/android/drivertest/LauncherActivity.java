package com.example.android.drivertest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUserName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
        mUserName = findViewById(R.id.user_name);
        mUserName.setOnClickListener(this);
        Button startQuiz = findViewById(R.id.start_button);
        startQuiz.setOnClickListener(this);

        RelativeLayout relativeLayout = findViewById(R.id.launcher_main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    // Buttons on click listeners

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button: // start main activity and send user name data
                Button startQuiz = findViewById(R.id.start_button);
                final Animator mButtonAnimationA = AnimatorInflater.
                        loadAnimator(LauncherActivity.this, R.animator.button_click_anim);
                mButtonAnimationA.setTarget(startQuiz);
                mButtonAnimationA.start();

                String name = mUserName.getText().toString();
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", name);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

            case R.id.settings_button: // Start AddQuizActivity, add new records to database
                Button settingButton = findViewById(R.id.settings_button);
                final Animator mButtonAnimationC = AnimatorInflater.
                        loadAnimator(LauncherActivity.this, R.animator.button_click_anim);
                mButtonAnimationC.setTarget(settingButton);

                mButtonAnimationC.start();
                startAddToDatabaseActivity();

                break;
        }

        // Clear hint text on click listener.

        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setHint(null);
            }
        });
    }

    /**
     * Start add to database activity.
     */

    private void startAddToDatabaseActivity() {
        Intent intent = new Intent(this, AddQuizActivity.class);
        startActivity(intent); // start Main activity

    }

    /**
     * Handle device configuration change.
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
