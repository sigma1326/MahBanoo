package com.simorgh.redcalendar.View.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.redcalendar.Model.database.CycleRepository;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.View.main.MainActivity;
import com.simorgh.redcalendar.ViewModel.register.CycleRegisterViewModel;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

public class QuestionsActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener
        , Step1Fragment.OnLastCycleDaySelectedListener
        , Step2Fragment.OnRedDaysCountSelectedListener
        , Step3Fragment.OnGrayDaysCountSelectedListener
        , Step4Fragment.OnYellowDaysCountSelectedListener
        , Step5Fragment.OnBirthDaySelectedListener {

    private Button nextButton;
    private Button forgetButton;
    private ImageButton prevButton;
    private TextView stepTitle;
    private TextView stepFractionTitle;
    private ProgressBar progressBar;
    private NavController navController;
    private View divider;
    private CycleRegisterViewModel cycleRegisterViewModel;
    private CycleRepository cycleRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycleRepository = new CycleRepository((Application) getApplicationContext());
        if (cycleRepository.getCycleData() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        setContentView(R.layout.activity_questions);

        cycleRegisterViewModel = ViewModelProviders.of(this).get(CycleRegisterViewModel.class);
        cycleRepository = new CycleRepository((Application) getApplicationContext());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransans_medium.ttf");

        nextButton = findViewById(R.id.ms_btn_next);
        forgetButton = findViewById(R.id.ms_btnForget);
        prevButton = findViewById(R.id.ms_prevButton);
        stepTitle = findViewById(R.id.ms_tvStepTitle);
        stepFractionTitle = findViewById(R.id.ms_tvStepFraction);
        progressBar = findViewById(R.id.ms_progressBar);
        divider = findViewById(R.id.view_divider);

        nextButton.setTypeface(typeface);
        forgetButton.setTypeface(typeface);

        navController = Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment);
        Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment).addOnDestinationChangedListener(this);
        nextButton.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.step1:
                    navController.navigate(R.id.action_step1_to_step2);
                    runForgetButtonAnim(true);
                    runPrevButtonAnim(true);
                    break;
                case R.id.step2:
                    navController.navigate(R.id.action_step2_to_step3);
                    break;
                case R.id.step2forget:
                    navController.navigate(R.id.action_step2forget_to_step3);
                    runForgetButtonAnim(true);
                    break;
                case R.id.step3:
                    navController.navigate(R.id.action_step3_to_step4);
                    break;
                case R.id.step3forget:
                    navController.navigate(R.id.action_step3forget_to_step4);
                    runForgetButtonAnim(true);
                    break;
                case R.id.step4:
                    navController.navigate(R.id.action_step4_to_step5);
                    runForgetButtonAnim(false);
                    break;
                case R.id.step4forget:
                    navController.navigate(R.id.action_step4forget_to_step5);
                    runForgetButtonAnim(false);
                    break;
                case R.id.step5:
                    Cycle cycle = new Cycle();
                    cycle.setCycleID(1);
                    cycle.setYellowDaysCount(cycleRegisterViewModel.getYellowDaysCount());
                    cycle.setRedDaysCount(cycleRegisterViewModel.getRedDaysCount());
                    cycle.setGrayDaysCount(cycleRegisterViewModel.getGrayDaysCount());
                    cycle.setBirthYear(cycleRegisterViewModel.getBirthYear());
                    Calendar calendar = cycleRegisterViewModel.getLastCycleEndDay();
                    calendar.add(Calendar.DAY_OF_MONTH, -1 * cycleRegisterViewModel.getRedDaysCount());
                    cycle.setStartDate(calendar);
                    cycleRepository.insertCycle(cycle);
                    navController.navigate(R.id.action_step5_to_mainActivity);
                    finish();
                    break;
                default:
            }
        });

        forgetButton.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.step2:
                    navController.navigate(R.id.action_step2_to_step2forget);
                    runForgetButtonAnim(false);
                    break;
                case R.id.step3:
                    navController.navigate(R.id.action_step3_to_step3forget);
                    runForgetButtonAnim(false);
                    break;
                case R.id.step4:
                    navController.navigate(R.id.action_step4_to_step4forget);
                    runForgetButtonAnim(false);
                    break;
            }
        });

        prevButton.setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void runPrevButtonAnim(boolean visible) {
        if (visible) {
            prevButton.setVisibility(View.VISIBLE);
            prevButton.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    prevButton.setVisibility(View.VISIBLE);
                }
            }).start();
        } else {
            prevButton.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    prevButton.setVisibility(View.GONE);
                }
            }).start();
        }
    }

    private void runForgetButtonAnim(boolean visible) {
        int h2;
        int h1;
        if (visible) {
            if (forgetButton.getVisibility() == View.VISIBLE) {
                return;
            }


            h1 = forgetButton.getHeight();
            h2 = forgetButton.getHeight() + divider.getHeight();
            forgetButton.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);

            forgetButton.setTranslationY(h1);
            divider.setTranslationY(h2);

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
                nextButton.setText(R.string.next_question);
                runProgressBarAnim(20);
                runForgetButtonAnim(false);
                runPrevButtonAnim(false);
                break;
            case R.id.step2:
                stepTitle.setText(R.string.step2_title);
                stepFractionTitle.setText(R.string.step2_fractionTitle);
                runProgressBarAnim(40);
                nextButton.setText(R.string.next_question);
                runForgetButtonAnim(true);
                break;
            case R.id.step2forget:
                stepTitle.setText(R.string.step2_title);
                stepFractionTitle.setText(R.string.step2_fractionTitle);
                runProgressBarAnim(40);
                nextButton.setText(R.string.next_question);
                break;
            case R.id.step3:
                stepTitle.setText(R.string.step3_title);
                stepFractionTitle.setText(R.string.step3_fractionTitle);
                runProgressBarAnim(60);
                nextButton.setText(R.string.next_question);
                runForgetButtonAnim(true);
                break;
            case R.id.step3forget:
                stepTitle.setText(R.string.step3_title);
                stepFractionTitle.setText(R.string.step3_fractionTitle);
                runProgressBarAnim(60);
                nextButton.setText(R.string.next_question);
                break;
            case R.id.step4:
                stepTitle.setText(R.string.step4_title);
                stepFractionTitle.setText(R.string.step4_fractionTitle);
                runProgressBarAnim(80);
                nextButton.setText(R.string.next_question);
                runForgetButtonAnim(true);
                break;
            case R.id.step4forget:
                stepTitle.setText(R.string.step4_title);
                stepFractionTitle.setText(R.string.step4_fractionTitle);
                runProgressBarAnim(80);
                nextButton.setText(R.string.next_question);
                runForgetButtonAnim(false);
                break;
            case R.id.step5:
                stepTitle.setText(R.string.step5_title);
                stepFractionTitle.setText(R.string.step5_fractionTitle);
                runProgressBarAnim(100);
                nextButton.setText(R.string.login_to_app);
                break;
            default:
        }
    }

    private void runProgressBarAnim(int i) {
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, progressBar.getProgress(), i);
        anim.setDuration(400);
        progressBar.startAnimation(anim);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return navController.navigateUp();
    }

    @Override
    public void onLastCycleDaySelected(Calendar calendar) {
        cycleRegisterViewModel.setLastCycleEndDay(calendar);
    }

    @Override
    public void onRedDaysCountSelected(int count) {
        cycleRegisterViewModel.setRedDaysCount(count);
    }

    @Override
    public void onGrayDaysCountSelected(int count) {
        cycleRegisterViewModel.setGrayDaysCount(count);
    }

    @Override
    public void onYellowDaysCountSelected(int count) {
        cycleRegisterViewModel.setYellowDaysCount(count);
    }

    @Override
    public void onBirthDaySelected(int year) {
        cycleRegisterViewModel.setBirthYear(year);
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
}
