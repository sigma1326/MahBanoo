package com.simorgh.cluecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.util.MonthViewAdapter;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarView extends LinearLayout {
    private int calendarType;
    private int monthViewType;
    private WeekDaysLabelView labelView;
    private MonthViewAdapter adapter;
    private RecyclerView recyclerView;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setOrientation(VERTICAL);
        labelView = new WeekDaysLabelView(getContext());
        labelView.setCalendarType(CalendarType.PERSIAN);
        addView(labelView);

        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, 2018);
        maxDate.set(Calendar.YEAR, 2019);

        recyclerView = new RecyclerView(getContext());
        addView(recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthViewAdapter(getContext(), CalendarType.PERSIAN, BaseMonthView.MonthViewTypeShowCalendar);
//        adapter.setRange(minDate,maxDate);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setDrawingCacheEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    public int getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
        labelView.setCalendarType(calendarType);
        adapter.setCalendarType(calendarType);
    }

    public int getMonthViewType() {
        return monthViewType;
    }

    public void setMonthViewType(int monthViewType) {
        this.monthViewType = monthViewType;
        adapter.setMonthViewType(monthViewType);
    }
}
