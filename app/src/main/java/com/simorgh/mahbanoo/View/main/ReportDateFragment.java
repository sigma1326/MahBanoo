package com.simorgh.mahbanoo.View.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cyclecalendar.view.CalendarView;
import com.simorgh.cyclecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.MakeReportViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ReportDateFragment extends Fragment implements ShowInfoMonthView.IsDayMarkedListener, BaseMonthView.OnDayClickListener, CalendarView.OnScrollListener {

    private MakeReportViewModel mViewModel;
    private CalendarView calendarView;
    private Button btnApplyChanges;
    private NavController navController;


    public static ReportDateFragment newInstance() {
        return new ReportDateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report_date_fragment, container, false);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment);


        calendarView = v.findViewById(R.id.calendar_view);
        calendarView.setRange(AppManager.minDate, AppManager.maxDate);
        calendarView.setMonthViewType(BaseMonthView.MonthViewTypeSetStartDay);
        calendarView.setCalendarType(CalendarType.PERSIAN);

        calendarView.setIsDayMarkedListener(this);
        calendarView.setOnDayClickListener(this);
        calendarView.setOnScrollListener(this);
        btnApplyChanges = v.findViewById(R.id.btn_apply_changes);
        btnApplyChanges.setOnClickListener(v1 -> navController.navigateUp());


        return v;
    }

    @Override
    public void onDestroyView() {
        calendarView = null;
        btnApplyChanges = null;
        navController = null;
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MakeReportViewModel.class);
        if (mViewModel.isRangeStart()) {
            mViewModel.getRangeStartLive().observe(this, calendar -> {
                if (calendarView != null && calendar != null) {
                    calendarView.setSelectedDate(calendar);
                    calendarView.scrollToCurrentDate(mViewModel.getRangeStartLive().getValue());
                    calendarView.setCycleData(new CycleData(0, 0, 0, calendar, calendar));
                }
            });
        } else {
            mViewModel.getRangeEndLive().observe(this, calendar -> {
                if (calendarView != null && calendar != null) {
                    calendarView.setSelectedDate(calendar);
                    calendarView.scrollToCurrentDate(mViewModel.getRangeEndLive().getValue());
                    calendarView.setCycleData(new CycleData(0, 0, 0, calendar, calendar));
                }
            });
        }
    }


    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        if (mViewModel.isRangeStart()) {
            if (day.before(mViewModel.getRangeEndLive().getValue())) {
                mViewModel.setRangeStart(day);
            }
        } else {
            if (day.before(AppManager.getCalendarInstance())) {
                mViewModel.setRangeEnd(day);
            }
        }
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
    public List<Integer> getMarkedDays(Calendar day) {
        return new ArrayList<>();
    }
}
