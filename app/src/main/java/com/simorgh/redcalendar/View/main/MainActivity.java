package com.simorgh.redcalendar.View.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.simorgh.bottombar.BottomBar;
import com.simorgh.cycleview.CycleView;
import com.simorgh.redcalendar.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity implements BottomBar.OnItemClickListener
        , BottomBar.OnCircleItemClickListener
        , NavController.OnDestinationChangedListener
        , CycleViewFragment.OnButtonChangeClickListener
        , CycleViewFragment.OnDayTypeChangedListener {

    private AppCompatTextView titleText;
    private NavController navController;
    private BottomBar bottomBar;
    private ImageView imgInfo;
    private ImageButton imgBack;
    private int dayType = CycleView.TYPE_GRAY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock orientation into landscape.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        navController = Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment);
        Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment).addOnDestinationChangedListener(this);
        titleText = findViewById(R.id.tv_toolbarTitle);
        imgInfo = findViewById(R.id.img_info);
        imgBack = findViewById(R.id.img_back);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemClickListener(this);
        bottomBar.setCircleItemClickListener(this);
        imgBack.setOnClickListener(v -> navController.navigateUp());

        imgInfo.setOnClickListener(v -> {
            if (isImgInfoAnimRunning) {
                return;
            }
            switch (dayType) {
                case CycleView.TYPE_RED:
                    titleText.setText(getString(R.string.red_info_title));
                    break;
                case CycleView.TYPE_GRAY:
                    break;
                case CycleView.TYPE_GREEN:
                    titleText.setText(getString(R.string.green_info_title));
                    break;
                case CycleView.TYPE_GREEN2:
                    titleText.setText(getString(R.string.green2_info_title));
                    break;
                case CycleView.TYPE_YELLOW:
                    titleText.setText(getString(R.string.yellow_info_title));
                    break;
                default:

            }
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.home) {
                CycleViewFragmentDirections.ActionHomeToCycleInfo action = CycleViewFragmentDirections.actionHomeToCycleInfo();
                action.setDayType(dayType);
                navController.navigate(action);
            }
        });
    }

    @Override
    public void onClick(BottomBar.BottomItem item, boolean fromUser) {
        titleText.setText(item.getText());
        if (fromUser) {
            bottomBarClicked(false, item);
        }
    }

    @Override
    public void onClick(boolean fromUser) {
        if (fromUser) {
            bottomBarClicked(true, null);
            titleText.setText(getString(R.string.addInformation));

        }
    }

    private void bottomBarClicked(boolean isCircle, BottomBar.BottomItem item) {
        if (isCircle || item == null) {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.home:
                    navController.navigate(R.id.action_home_to_addNote);
                    break;
                case R.id.calendar:
                    navController.navigate(R.id.action_calendar_to_addNote);
                    break;
                case R.id.profile:
                    navController.navigate(R.id.action_profile_to_addNote);
                    break;
                case R.id.settings:
                    navController.navigate(R.id.action_settings_to_addNote);
                    break;
            }
        } else {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.home:
                    switch (item.getIndex()) {
                        case 4:
                            break;
                        case 3:
                            navController.navigate(R.id.action_home_to_calendar);
                            break;
                        case 2:
                            navController.navigate(R.id.action_home_to_profile);
                            break;
                        case 1:
                            navController.navigate(R.id.action_home_to_settings);
                            break;
                    }
                    break;
                case R.id.calendar:
                    switch (item.getIndex()) {
                        case 4:
                            navController.popBackStack(R.id.home, false);
                            break;
                        case 3:
                            break;
                        case 2:
                            navController.navigate(R.id.action_calendar_to_profile);
                            break;
                        case 1:
                            navController.navigate(R.id.action_calendar_to_settings);
                            break;
                    }
                    break;
                case R.id.profile:
                    switch (item.getIndex()) {
                        case 4:
                            navController.popBackStack(R.id.home, false);
                            break;
                        case 3:
                            navController.navigate(R.id.action_profile_to_calendar);
                            break;
                        case 2:
                            break;
                        case 1:
                            navController.navigate(R.id.action_profile_to_settings);
                            break;
                    }
                    break;
                case R.id.settings:
                    switch (item.getIndex()) {
                        case 4:
                            navController.popBackStack(R.id.home, false);
                            break;
                        case 3:
                            navController.navigate(R.id.action_settings_to_calendar);
                            break;
                        case 2:
                            navController.navigate(R.id.action_settings_to_profile);
                            break;
                        case 1:
                            break;
                    }
                    break;
                case R.id.addNote:
                    switch (item.getIndex()) {
                        case 4:
                            navController.popBackStack(R.id.home, false);
                            break;
                        case 3:
                            break;
                        case 2:
                            break;
                        case 1:
                            break;
                    }
                    break;
            }
        }
    }


    private void runBackButtonAnim(boolean visible) {
        if (visible) {
            imgBack.setVisibility(View.VISIBLE);
            imgBack.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    imgBack.setVisibility(View.VISIBLE);
                }
            }).start();
        } else {
            imgBack.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    imgBack.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
    }

    private boolean isImgInfoAnimRunning = false;

    private void runImageInfoAnim(boolean visible) {
        Log.d("debug13", visible + ":" + isImgInfoAnimRunning);
        if (isImgInfoAnimRunning) {
            return;
        }
        if (imgInfo == null) {
            return;
        }
        if (visible) {
            if (imgInfo.getVisibility() == View.VISIBLE) {
                return;
            }
            imgInfo.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isImgInfoAnimRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imgInfo.setVisibility(View.VISIBLE);
                    isImgInfoAnimRunning = false;
                }
            }).start();
        } else {
            if (imgInfo.getVisibility() == View.INVISIBLE) {
                return;
            }
            imgInfo.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isImgInfoAnimRunning = true;
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    imgInfo.setVisibility(View.INVISIBLE);
                    isImgInfoAnimRunning = false;
                }
            }).start();
        }
    }

    private void runBottomBarAnim(boolean visible) {
        int h1;
        if (visible) {
            if (bottomBar.getVisibility() == View.VISIBLE) {
                return;
            }


            h1 = bottomBar.getHeight();
            bottomBar.setVisibility(View.VISIBLE);

            bottomBar.setTranslationY(h1);
            bottomBar.animate()
                    .translationYBy(-h1)
                    .alpha(1.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }
                    }).start();

        } else {
            if (bottomBar.getVisibility() == View.INVISIBLE) {
                return;
            }
            h1 = bottomBar.getHeight();
            bottomBar.animate()
                    .translationYBy(h1)
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            bottomBar.setVisibility(View.INVISIBLE);
                        }
                    }).start();
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        runImageInfoAnim(Objects.requireNonNull(controller.getCurrentDestination()).getId() == R.id.home);
        switch (Objects.requireNonNull(controller.getCurrentDestination()).getId()) {
            case R.id.home:
                if (bottomBar != null) {
                    imgInfo.setVisibility(View.VISIBLE);
                    bottomBar.setSelectedIndex(4);
                    runBottomBarAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.calendar:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(3);
                    runBottomBarAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.profile:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(2);
                    runBottomBarAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.settings:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(1);
                    runBottomBarAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.addNote:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(-1);
                    runBottomBarAnim(false);
                    runBackButtonAnim(true);
                }
                break;

            case R.id.change_cycle:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(-1);
                    runBottomBarAnim(false);
                    runBackButtonAnim(true);
                }
                break;
            case R.id.cycleInfo:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(-1);
                    runBottomBarAnim(false);
                    runBackButtonAnim(true);
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return navController.navigateUp();
    }

    @Override
    public void onButtonChangeClicked() {
        titleText.setText(getString(R.string.change_cycle_days));
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.home) {
            navController.navigate(R.id.action_home_to_change_cycle);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onDayTypeChanged(int dayType) {
        this.dayType = dayType;
        ColorFilter colorFilter = new LightingColorFilter(Color.TRANSPARENT, getColor(R.color.type_gray));

        switch (dayType) {
            case CycleView.TYPE_RED:
                colorFilter = new LightingColorFilter(Color.TRANSPARENT, getColor(R.color.type_red));
                break;
            case CycleView.TYPE_GRAY:
                break;
            case CycleView.TYPE_GREEN:
                colorFilter = new LightingColorFilter(Color.TRANSPARENT, getColor(R.color.type_green));
                break;
            case CycleView.TYPE_GREEN2:
                colorFilter = new LightingColorFilter(Color.TRANSPARENT, getColor(R.color.type_green));
                break;
            case CycleView.TYPE_YELLOW:
                colorFilter = new LightingColorFilter(Color.TRANSPARENT, getColor(R.color.type_yellow));
                break;
        }
        imgInfo.setColorFilter(colorFilter);
        runImageInfoAnim(dayType != CycleView.TYPE_GRAY);
    }
}
