package com.simorgh.mahbanoo.View.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cyclecalendar.view.CalendarView;
import com.simorgh.cyclecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.register.Step1ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step1Fragment extends Fragment implements BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener {

    private Step1ViewModel mViewModel;
    private CalendarView calendarView;
    private Calendar min = AppManager.getCalendarInstance();
    private Calendar max = AppManager.getCalendarInstance();
    private Calendar today = AppManager.getCalendarInstance();
    private OnLastCycleDaySelectedListener onLastCycleDaySelected;

    public static Step1Fragment newInstance() {
        return new Step1Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (calendarView == null) {
            min.set(Calendar.YEAR, today.get(Calendar.YEAR) - 1);
            min.set(Calendar.MONTH, 0);
            max.add(Calendar.MONTH, 1);
            calendarView = new CalendarView(getContext(), BaseMonthView.MonthViewTypeSetStartDay
                    , CalendarType.PERSIAN, new CycleData(0, 0, 0, today, today), min, max);
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
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onLastCycleDaySelected = (OnLastCycleDaySelectedListener) context;
    }

    @Override
    public void onDestroyView() {
        if (onLastCycleDaySelected != null) {
            onLastCycleDaySelected.onLastCycleDaySelected(mViewModel.getDay());
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        onLastCycleDaySelected = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        calendarView = null;
        super.onDestroy();
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        mViewModel.setDay(day);
    }

    @Override
    public List<Integer> getMarkedDays(Calendar day) {
        return new ArrayList<>();
    }


    public interface OnLastCycleDaySelectedListener {
        void onLastCycleDaySelected(Calendar calendar);
    }
}
