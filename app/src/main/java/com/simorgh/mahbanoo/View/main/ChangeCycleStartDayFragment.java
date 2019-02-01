package com.simorgh.mahbanoo.View.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cyclecalendar.view.CalendarView;
import com.simorgh.cyclecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.cycleutils.CycleUtils;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.User;
import com.simorgh.databaseutils.model.UserWithCycles;
import com.simorgh.mahbanoo.Model.AndroidUtils;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.CycleViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ChangeCycleStartDayFragment extends Fragment implements ShowInfoMonthView.IsDayMarkedListener, BaseMonthView.OnDayClickListener, CalendarView.OnScrollListener {

    private CycleViewModel mViewModel;
    private CalendarView calendarView;
    private Button btnApplyChanges;
    private NavController navController;
    private Typeface typeface;


    public static ChangeCycleStartDayFragment newInstance() {
        return new ChangeCycleStartDayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
        }
    }

    @Override
    public void onDestroyView() {
        try {
            if (isAnimRunning) {
                btnApplyChanges.getAnimation().cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendarView = null;
        btnApplyChanges = null;
        navController = null;
        super.onDestroyView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.change_cycle_start_day_fragment, container, false);
        calendarView = v.findViewById(R.id.calendar_view);

        calendarView.setRange(AppManager.minDate, AppManager.maxDate);
        calendarView.setMonthViewType(BaseMonthView.MonthViewTypeChangeDays);
        calendarView.setCalendarType(CalendarType.PERSIAN);

        calendarView.setIsDayMarkedListener(this);
        calendarView.setOnDayClickListener(this);
        calendarView.setOnScrollListener(this);
        calendarView.scrollToCurrentDate(AppManager.getCalendarInstance());
        if (getActivity() != null) {
            navController = Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment);
        }

        btnApplyChanges = v.findViewById(R.id.btn_apply_changes);
        btnApplyChanges.setTypeface(typeface);
        btnApplyChanges.setOnClickListener(v1 -> {
            AppManager.getExecutor().execute(() -> {
                UserWithCycles userWithCycles = mViewModel.getUserWithCyclesLiveData().getValue();
                if (userWithCycles != null) {
                    Cycle oldCycle = userWithCycles.getCurrentCycle();

                    Cycle newCycle = userWithCycles.getCurrentCycle().clone();
                    newCycle.setStartDate(mViewModel.getSelectedStartDate());
                    newCycle.setEndDate(null);

                    Calendar endDate = Calendar.getInstance();
                    endDate.clear();
                    endDate.setTimeInMillis(mViewModel.getSelectedStartDate().getTimeInMillis());
                    endDate.add(Calendar.DAY_OF_MONTH, -1);
                    oldCycle.setEndDate(endDate);


                    User user = userWithCycles.getUser();
                    user.setCurrentCycle(newCycle.getStartDate());

                    mViewModel.updateCycle(oldCycle);
                    mViewModel.updateCycle(newCycle);
                    mViewModel.updateUser(user);

                    AndroidUtils.runOnUIThread(() -> {
                        navController.navigateUp();
                    });
                }
            });
        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getUserWithCyclesLiveData().observe(this, userWithCycles -> {
            if (calendarView != null && userWithCycles != null) {
                Cycle cycle = userWithCycles.getCurrentCycle();
                User user = userWithCycles.getUser();
                mViewModel.setUser(user);
                mViewModel.setCycle(cycle);
                calendarView.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate(), cycle.getEndDate()));
                calendarView.setCycleDataList(CycleUtils.getCycleDataList(userWithCycles.getCycles()));
                calendarView.setSelectedDate(cycle.getCurrentCycleStart(AppManager.getCalendarInstance()));
                calendarView.scrollToCurrentDate(cycle.getCurrentCycleStart(Calendar.getInstance()));
                Log.d(AppManager.TAG, cycle.toString());
                calendarView.setShowInfo(user.isShowCycleDays());
            }
        });
    }


    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        mViewModel.setSelectedStartDate(day);
    }

    @Override
    public void onScrolled(boolean isScrolling) {
        runButtonChangeAnim(!isScrolling);
    }

    private boolean isAnimRunning = false;
    private Queue<Boolean> booleanQueue = new LinkedList<>();

    private void runButtonChangeAnim(boolean visible) {
        if (btnApplyChanges == null) {
            return;
        }
        int h1;
        if (isAnimRunning) {
            if (visible) {
                booleanQueue.add(true);
            }
            return;
        }
        if (visible) {
            if (btnApplyChanges.getVisibility() == View.VISIBLE) {
                return;
            }


            h1 = btnApplyChanges.getHeight();
            btnApplyChanges.setVisibility(View.VISIBLE);

            btnApplyChanges.setTranslationY(h1);
            btnApplyChanges.animate()
                    .translationYBy(-h1)
                    .alpha(1.0f)
                    .setDuration(400)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimRunning = false;
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimRunning = true;
                        }
                    }).start();

        } else {
            if (btnApplyChanges.getVisibility() == View.GONE) {
                return;
            }
            h1 = btnApplyChanges.getHeight();
            btnApplyChanges.animate()
                    .translationYBy(h1)
                    .alpha(0.0f)
                    .setDuration(400)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnApplyChanges.setVisibility(View.GONE);
                            isAnimRunning = false;
                            if (!booleanQueue.isEmpty()) {
                                runButtonChangeAnim(booleanQueue.poll());
                            }
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimRunning = true;
                        }
                    }).start();
        }
    }

    @Override
    public List<Integer> getMarkedDays(Calendar start, Calendar end) {
        return new ArrayList<>();
    }
}
