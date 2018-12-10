package com.simorgh.cluecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.util.MonthViewAdapter;

import java.util.Calendar;

import androidx.annotation.NonNull;
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
    private BaseMonthView.OnDayClickListener onDayClickListener;
    private RecyclerView.LayoutManager layoutManager;
    private Calendar min;
    private Calendar max;
    private ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener;


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

        recyclerView = new RecyclerView(getContext());
        addView(recyclerView);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthViewAdapter(getContext(), calendarType, monthViewType);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
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

    public BaseMonthView.OnDayClickListener getOnDayClickListener() {
        return onDayClickListener;
    }

    public void setOnDayClickListener(BaseMonthView.OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
        adapter.setOnDayClickListener(onDayClickListener);
    }

    public void setRange(@NonNull Calendar min, @NonNull Calendar max) {
        this.min = min;
        this.max = max;
        adapter.setRange(min, max);
    }

    public void scrollToCurrentDate(Calendar now) {
        if (layoutManager != null) {
            int position = 0;
            switch (calendarType) {
                case CalendarType.GREGORIAN:
                    int diffYear = now.get(Calendar.YEAR) - min.get(Calendar.YEAR);
                    int diffMonth = now.get(Calendar.MONTH) - min.get(Calendar.MONTH);
                    position = diffYear * 12 + diffMonth;
                    break;
                case CalendarType.PERSIAN:
                    diffYear = CalendarTool.GregorianToPersian(now).getPersianYear() - CalendarTool.GregorianToPersian(min).getPersianYear();
                    diffMonth = CalendarTool.GregorianToPersian(now).getPersianMonth() - CalendarTool.GregorianToPersian(min).getPersianMonth();
                    position = diffYear * 12 + diffMonth;
                    break;
                case CalendarType.ARABIC:
                    diffYear = CalendarTool.GregorianToHijri(now).get(UmmalquraCalendar.YEAR) - CalendarTool.GregorianToHijri(min).get(UmmalquraCalendar.YEAR);
                    diffMonth = CalendarTool.GregorianToHijri(now).get(UmmalquraCalendar.MONTH) - CalendarTool.GregorianToHijri(min).get(UmmalquraCalendar.MONTH);
                    position = diffYear * 12 + diffMonth;
                    break;

            }
            layoutManager.scrollToPosition(position);
        }
    }

    public ShowInfoMonthView.IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
        adapter.setIsDayMarkedListener(isDayMarkedListener);
    }
}
