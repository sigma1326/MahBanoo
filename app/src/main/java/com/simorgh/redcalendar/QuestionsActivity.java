package com.simorgh.redcalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private Button nextButton;
    private Button forgetButton;
    private ImageButton prevButton;
    private TextView stepTitle;
    private TextView stepFractionTitle;
    private ProgressBar progressBar;
    private NavController navController;
    private View divider;
    private View fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransans_medium.ttf");

        nextButton = findViewById(R.id.ms_btn_next);
        forgetButton = findViewById(R.id.ms_btnForget);
        prevButton = findViewById(R.id.ms_prevButton);
        stepTitle = findViewById(R.id.ms_tvStepTitle);
        stepFractionTitle = findViewById(R.id.ms_tvStepFraction);
        progressBar = findViewById(R.id.ms_progressBar);
        divider = findViewById(R.id.view_divider);
        fragment = findViewById(R.id.stepper_nav_host_fragment);

        nextButton.setTypeface(typeface);
        forgetButton.setTypeface(typeface);

        progressBar.setProgress(20);
        navController = Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment);
        Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment).addOnDestinationChangedListener(this);
        nextButton.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.step1:
                    navController.navigate(R.id.action_step1_to_step2);
//                    progressBar.setProgress(40, true);
                    runAnim(true);
                    break;
                case R.id.step2:
                    navController.navigate(R.id.action_step2_to_step3);
                    break;
                case R.id.step2forget:
                    navController.navigate(R.id.action_step2forget_to_step3);
                    runAnim(true);
                    break;
                case R.id.step3:
                    navController.navigate(R.id.action_step3_to_step4);
                    break;
                case R.id.step3forget:
                    navController.navigate(R.id.action_step3forget_to_step4);
                    runAnim(true);
                    break;
                case R.id.step4:
                    navController.navigate(R.id.action_step4_to_step5);
                    runAnim(false);
                    break;
                case R.id.step5:
                    navController.navigate(R.id.action_step5_to_mainActivity);
                    break;
                default:
            }
        });

        forgetButton.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.step2:
                    navController.navigate(R.id.action_step2_to_step2forget);
                    runAnim(false);
                    break;
                case R.id.step3:
                    navController.navigate(R.id.action_step3_to_step3forget);
                    runAnim(false);
                    break;
            }
        });

        prevButton.setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    int h1, h2;

    private void runAnim(boolean visible) {
        if (visible) {
            if (forgetButton.getVisibility() == View.VISIBLE) {
                return;
            }
            forgetButton.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);

            forgetButton.animate()
                    .translationYBy(-h1)
                    .alpha(1.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }
                    }).start();

            divider.animate()
                    .translationYBy(-h2)
                    .alpha(1.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }
                    }).start();
        } else {
            if (forgetButton.getVisibility() == View.GONE) {
                return;
            }
            h1 = forgetButton.getHeight();
            h2 = forgetButton.getHeight() + divider.getHeight();
            forgetButton.animate()
                    .translationYBy(h1)
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            forgetButton.setVisibility(View.GONE);
                        }
                    }).start();

            divider.animate()
                    .translationYBy(h2)
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            divider.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        switch (Objects.requireNonNull(controller.getCurrentDestination()).getId()) {
            case R.id.step1:
                stepTitle.setText(R.string.step1_title);
                stepFractionTitle.setText(R.string.step1_fractionTitle);
                progressBar.setProgress(20);
                nextButton.setText(R.string.next_question);
                runAnim(false);
                break;
            case R.id.step2:
                stepTitle.setText(R.string.step2_title);
                stepFractionTitle.setText(R.string.step2_fractionTitle);
                progressBar.setProgress(40);
                nextButton.setText(R.string.next_question);
                runAnim(true);
                break;
            case R.id.step2forget:
                stepTitle.setText(R.string.step2_title);
                stepFractionTitle.setText(R.string.step2_fractionTitle);
                progressBar.setProgress(40);
                nextButton.setText(R.string.next_question);
                break;
            case R.id.step3:
                stepTitle.setText(R.string.step3_title);
                stepFractionTitle.setText(R.string.step3_fractionTitle);
                progressBar.setProgress(60);
                nextButton.setText(R.string.next_question);
                break;
            case R.id.step3forget:
                stepTitle.setText(R.string.step3_title);
                stepFractionTitle.setText(R.string.step3_fractionTitle);
                progressBar.setProgress(60);
                nextButton.setText(R.string.next_question);
                break;
            case R.id.step4:
                stepTitle.setText(R.string.step4_title);
                stepFractionTitle.setText(R.string.step4_fractionTitle);
                progressBar.setProgress(80);
                nextButton.setText(R.string.next_question);
                runAnim(true);
                break;
            case R.id.step5:
                stepTitle.setText(R.string.step5_title);
                stepFractionTitle.setText(R.string.step5_fractionTitle);
                progressBar.setProgress(100);
                nextButton.setText(R.string.login_to_app);
                break;
            default:
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}
