package com.simorgh.redcalendar.View;

import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.CalendarView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.CalendarViewModel;

import java.util.Calendar;

public class CalendarFragment extends Fragment implements ShowInfoMonthView.IsDayMarkedListener, BaseMonthView.OnDayClickListener {

    private CalendarViewModel mViewModel;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.calendar_fragment, container, false);

        CalendarView calendarView = new CalendarView(getActivity()
                , BaseMonthView.MonthViewTypeShowCalendar
                , CalendarType.PERSIAN, AppManager.clueData
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
    }

    @Override
    public boolean isDayMarked(Calendar day) {
        return false;
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {

    }
}
