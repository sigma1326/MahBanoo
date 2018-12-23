package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.CalendarView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.ClueData;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.ViewModel.main.CalendarViewModel;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;

import java.util.Calendar;

public class CalendarFragment extends Fragment implements ShowInfoMonthView.IsDayMarkedListener, BaseMonthView.OnDayClickListener {

    private CalendarViewModel mViewModel;
    private CycleViewModel cycleViewModel;
    private CalendarView calendarView;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarView = new CalendarView(getActivity()
                , BaseMonthView.MonthViewTypeShowCalendar
                , CalendarType.PERSIAN, null
                , AppManager.minDate, AppManager.maxDate);

        calendarView.setIsDayMarkedListener(this);
        calendarView.setOnDayClickListener(this);
        calendarView.scrollToCurrentDate(Calendar.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            calendarView.setElevation(10);
        }
        return calendarView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        cycleViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        cycleViewModel.getCycleLiveData().observe(this, cycle -> {
            if (calendarView != null && cycle != null) {
                calendarView.setClueData(new ClueData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

    @Override
    public boolean isDayMarked(Calendar day) {
        return false;
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {

    }
}
