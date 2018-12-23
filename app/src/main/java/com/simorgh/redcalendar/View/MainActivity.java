package com.simorgh.redcalendar.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.simorgh.bottombar.BottomBar;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.clueview.ClueView;
import com.simorgh.clueview.OnViewDataChangedListener;
import com.simorgh.redcalendar.R;
import com.simorgh.weekdaypicker.ClueData;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements BottomBar.OnItemClickListener, BottomBar.OnCircleItemClickListener, NavController.OnDestinationChangedListener {

    private AppCompatTextView titleText;
    private NavController navController;
    private BottomBar bottomBar;
    private ImageView imgInfo;
    private ImageButton imgBack;


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

        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.DAY_OF_MONTH, 26);
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
        if (isCircle) {
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
//                            NavOptions navOptions = new NavOptions.Builder()
//                                    .setPopUpTo(R.id.home,
//                                            true).build();
                            navController.navigate(R.id.action_calendar_to_home, null);
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
                            navController.navigate(R.id.action_profile_to_home);
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
                            navController.navigate(R.id.action_settings_to_home);
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

    private void runBottomBarnAnim(boolean visible) {
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
            if (bottomBar.getVisibility() == View.GONE) {
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
                            bottomBar.setVisibility(View.GONE);
                        }
                    }).start();
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (imgInfo != null) {
            imgInfo.setVisibility(View.GONE);
        }
        switch (Objects.requireNonNull(controller.getCurrentDestination()).getId()) {
            case R.id.home:
                if (bottomBar != null) {
                    imgInfo.setVisibility(View.VISIBLE);
                    bottomBar.setSelectedIndex(4);
                    runBottomBarnAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.calendar:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(3);
                    runBottomBarnAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.profile:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(2);
                    runBottomBarnAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.settings:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(1);
                    runBottomBarnAnim(true);
                    runBackButtonAnim(false);
                }
                break;
            case R.id.addNote:
                if (bottomBar != null) {
                    bottomBar.setSelectedIndex(-1);
                    runBottomBarnAnim(false);
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
}
