package com.simorgh.mahbanoo.View.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.User;
import com.simorgh.forceupdate.ForceUpdateApp;
import com.simorgh.forceupdate.OnUpdateStatusReceiveListener;
import com.simorgh.forceupdate.repo.model.CheckUpdateStatusResponse;
import com.simorgh.mahbanoo.Model.AndroidUtils;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.Model.Logger;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.View.main.MainActivity;
import com.simorgh.mahbanoo.ViewModel.register.CycleRegisterViewModel;
import com.simorgh.sweetalertdialog.SweetAlertDialog;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onStart() {
        super.onStart();


        ForceUpdateApp.getInstace(getString(R.string.force_update_api_key)).checkUpdateStatus(this, new OnUpdateStatusReceiveListener() {
            @Override
            public void onForceUpdateStatus(CheckUpdateStatusResponse checkUpdateStatusResponse) {
                String download = "";
                switch (checkUpdateStatusResponse.getType()) {
                    case 0:
                        download = "دانلود از کافه بازار";
                        break;
                    case 1:
                        download = "دانلود از google play";
                        break;
                    case 2:
                        download = "دانلود مستقیم";
                        break;
                    case 3:
                        download = "دانلود از تمامی مارکت‌ها";
                        break;
                    case 4:
                        download = "تلگرام";
                        break;
                    default:
                }

                new SweetAlertDialog(QuestionsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(checkUpdateStatusResponse.getMessage())
                        .setContentText(download)
                        .setConfirmText("باشه")
                        .setCancelText("بستن برنامه")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            init();
                        })
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        })
                        .show();

            }

            @Override
            public void onOptionalUpdateStatus(CheckUpdateStatusResponse checkUpdateStatusResponse) {
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getLink());
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getUserMessage());
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getMessage());
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getMoreInfo());
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getStatus());
                Logger.d("onOptionalUpdateStatus: " + checkUpdateStatusResponse.getType());

                String download = "";
                switch (checkUpdateStatusResponse.getType()) {
                    case 0:
                        download = "دانلود از کافه بازار";
                        break;
                    case 1:
                        download = "دانلود از google play";
                        break;
                    case 2:
                        download = "دانلود مستقیم";
                        break;
                    case 3:
                        download = "دانلود از تمامی مارکت‌ها";
                        break;
                    case 4:
                        download = "تلگرام";
                        break;
                    default:
                }

                new SweetAlertDialog(QuestionsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(checkUpdateStatusResponse.getMessage())
                        .setContentText(download)
                        .setConfirmText("باشه")
                        .setCancelText("بی‌خیال")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            init();
                        })
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        })
                        .show();
            }

            @Override
            public void onNoUpdateStatus(Boolean canCheck) {
                Logger.d("onNoUpdateStatus: " + canCheck);
                init();
            }
        });

    }


    @Override
    protected void onDestroy() {
        stepTitle = null;
        stepFractionTitle = null;
        prevButton = null;
        nextButton = null;
        forgetButton = null;
        divider = null;
        navController = null;
        super.onDestroy();
    }

    private void init() {

        AppManager.getExecutor().execute(() -> {
            if (AppManager.getCycleRepository().getCycleData() != null) {
                AndroidUtils.runOnUIThread(() -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            } else {
                AndroidUtils.runOnUIThread(() -> {
                    setContentView(R.layout.activity_questions);

                    cycleRegisterViewModel = ViewModelProviders.of(this).get(CycleRegisterViewModel.class);

                    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iransans_medium.ttf");

                    nextButton = findViewById(R.id.ms_btn_next);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        nextButton.setBackgroundDrawable(getDrawable(R.drawable.btn_next_step_ripple_background));
                    } else {
                        nextButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.btn_next_step_ripple_background_api19));
                    }
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
                                AppManager.getExecutor().execute(() -> {
                                    Cycle cycle = new Cycle();
                                    cycle.setYellowDaysCount(cycleRegisterViewModel.getYellowDaysCount());
                                    cycle.setRedDaysCount(cycleRegisterViewModel.getRedDaysCount());
                                    cycle.setGrayDaysCount(cycleRegisterViewModel.getGrayDaysCount());
                                    Calendar calendar = cycleRegisterViewModel.getLastCycleEndDay();
                                    calendar.add(Calendar.DAY_OF_MONTH, -1 * cycleRegisterViewModel.getRedDaysCount() + 1);
                                    cycle.setStartDate(calendar);
                                    cycle.setEndDate(null);
                                    cycle.setUserId(1);
                                    AppManager.getCycleRepository().insertCycle(cycle);
                                    User user = new User();
                                    user.setBirthYear(cycleRegisterViewModel.getBirthYear());
                                    user.setCurrentCycle(calendar);
                                    AppManager.getCycleRepository().insertUser(user);

                                    AndroidUtils.runOnUIThread(() -> {
                                        navController.navigate(R.id.action_step5_to_mainActivity);
                                        finish();
                                    });
                                });

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

                    prevButton.setOnClickListener(v -> navController.navigateUp());
                });
            }
        });

    }

    private void runPrevButtonAnim(boolean visible) {
        if (prevButton == null) {
            return;
        }
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
        if (forgetButton == null || divider == null) {
            return;
        }
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
        if (progressBar == null) {
            return;
        }
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, progressBar.getProgress(), i);
        anim.setDuration(200);
        progressBar.startAnimation(anim);
    }

    @Override
    public boolean onSupportNavigateUp() {
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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
