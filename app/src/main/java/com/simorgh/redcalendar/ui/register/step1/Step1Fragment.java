package com.simorgh.redcalendar.ui.register.step1;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step1Fragment extends Fragment implements  BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener {

    private Step1ViewModel mViewModel;
    private CalendarView calendarView;
    private Calendar min = Calendar.getInstance();
    private Calendar max = Calendar.getInstance();
    private Calendar today = Calendar.getInstance();

    public static Step1Fragment newInstance() {
        return new Step1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (calendarView == null) {
            min.set(Calendar.YEAR, 2018);
            min.set(Calendar.MONTH, 0);
            max.set(Calendar.YEAR, 2020);
            max.set(Calendar.MONTH, 10);
            calendarView = new CalendarView(getContext(), BaseMonthView.MonthViewTypeSetStartDay
                    , CalendarType.PERSIAN, new ClueData(5, 28, 4, today), min, max);
            calendarView.setOnDayClickListener(this);
            calendarView.setIsDayMarkedListener(this);

            calendarView.scrollToCurrentDate(today);
            calendarView.setWeekDaysViewBackgroundColor(getResources().getColor(R.color.my_type_gray_2));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return calendarView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step1ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {

    }

    @Override
    public boolean isDayMarked(Calendar day) {
        return false;
    }
}
