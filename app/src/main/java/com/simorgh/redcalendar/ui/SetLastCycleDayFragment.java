package com.simorgh.redcalendar.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.util.ClueData;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.CalendarView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.redcalendar.R;

import java.util.Calendar;

public class SetLastCycleDayFragment extends Fragment implements BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener {

    private SetLastCycleDayViewModel mViewModel;
    private CalendarView calendarView;
    private Calendar min = Calendar.getInstance();
    private Calendar max = Calendar.getInstance();
    private Calendar today = Calendar.getInstance();

    public static SetLastCycleDayFragment newInstance() {
        return new SetLastCycleDayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (calendarView == null) {
            calendarView = new CalendarView(getContext());
            calendarView.setOnDayClickListener(this);
            calendarView.setIsDayMarkedListener(this);
            calendarView.setMonthViewType(BaseMonthView.MonthViewTypeSetStartDay);
            calendarView.setCalendarType(CalendarType.PERSIAN);
            calendarView.setClueData(new ClueData(5, 28, 4, today));
            min.set(Calendar.YEAR, 2018);
            min.set(Calendar.MONTH, 0);
            max.set(Calendar.YEAR, 2020);
            max.set(Calendar.MONTH, 10);
            calendarView.setRange(min, max);
            calendarView.scrollToCurrentDate(today);
            calendarView.setWeekDaysViewBackgroundColor(getResources().getColor(R.color.my_type_gray_2));
        }

        return calendarView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetLastCycleDayViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {

    }

    @Override
    public boolean isDayMarked(int day) {
        return false;
    }
}
