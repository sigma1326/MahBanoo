package com.simorgh.cyclecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.hijricalendar.UmmalquraCalendar;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.cyclecalendar.util.MonthViewAdapter;
import com.simorgh.cycleutils.CycleData;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarView extends LinearLayout {
    private int calendarType;
    private int monthViewType;
    private WeekDaysLabelView weekDaysLabelView;
    private MonthViewAdapter adapter;
    private BaseMonthView.OnDayClickListener onDayClickListener;
    private RecyclerView.LayoutManager layoutManager;
    private Calendar min;
    private Calendar max;
    private ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener;
    private CycleData cycleData;
    private int weekDaysViewBackgroundColor = -1;
    private OnScrollListener onScrollListener;
    private Calendar selectedDate = Calendar.getInstance();
    private boolean showInfo = true;
    private List<CycleData> cycleDataList = new LinkedList<>();


    public CalendarView(Context context) {
        super(context);
        this.min = Calendar.getInstance();
        this.max = Calendar.getInstance();
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.min = Calendar.getInstance();
        this.max = Calendar.getInstance();
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.min = Calendar.getInstance();
        this.max = Calendar.getInstance();
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.min = Calendar.getInstance();
        this.max = Calendar.getInstance();
        init();
    }

    public CalendarView(Context context, int monthViewType, int calendarType, CycleData cycleData, Calendar minDate, Calendar maxDate) {
        super(context);
        this.monthViewType = monthViewType;
        this.calendarType = calendarType;
        this.cycleData = cycleData;
        this.min = minDate;
        this.max = maxDate;
        init();
    }


    private void init() {
        setOrientation(VERTICAL);
        weekDaysLabelView = new WeekDaysLabelView(getContext());
        weekDaysLabelView.setCalendarType(CalendarType.PERSIAN);
        if (weekDaysViewBackgroundColor != -1) {
            weekDaysLabelView.setBackgroundColor(weekDaysViewBackgroundColor);
        }
        addView(weekDaysLabelView);

        RecyclerView recyclerView = new RecyclerView(getContext());
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        addView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthViewAdapter(getContext(), cycleData, calendarType, monthViewType, min, max);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onScrollListener != null) {
                    onScrollListener.onScrolled(newState != 0);
                    Log.d("debug13", "" + (newState != 0));
                }
            }
        });

    }

    public int getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
        weekDaysLabelView.setCalendarType(calendarType);
        adapter.setCalendarType(calendarType);
    }

    public int getMonthViewType() {
        return monthViewType;
    }

    public void setMonthViewType(int monthViewType) {
        this.monthViewType = monthViewType;
        adapter.setMonthViewType(monthViewType);
    }

    @Override
    public void setElevation(float elevation) {
        super.setElevation(elevation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            weekDaysLabelView.setElevation(elevation);
        }
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
        adapter.setShowInfo(showInfo);
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public int getWeekDaysViewBackgroundColor() {
        return weekDaysViewBackgroundColor;
    }

    public void setWeekDaysViewBackgroundColor(int weekDaysViewBackgroundColor) {
        this.weekDaysViewBackgroundColor = weekDaysViewBackgroundColor;
        weekDaysLabelView.setBackgroundColor(weekDaysViewBackgroundColor);
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

    public Calendar getSelctedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar selctedDate) {
        this.selectedDate.setTimeInMillis(selctedDate.getTimeInMillis());
        adapter.setSelectedDay(this.selectedDate);
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

    public CycleData getCycleData() {
        return cycleData;
    }

    public void setCycleData(CycleData cycleData) {
        this.cycleData = cycleData;
        adapter.setCycleData(cycleData);
    }

    public ShowInfoMonthView.IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
        adapter.setIsDayMarkedListener(isDayMarkedListener);
    }

    public void setCycleDataList(List<CycleData> cycleDataList) {
        if (this.cycleDataList == null) {
            this.cycleDataList = new LinkedList<>();
        }
        if (cycleDataList != null) {
            this.cycleDataList.addAll(cycleDataList);
            adapter.setCycleDataList(this.cycleDataList);
        }
    }

    public List<CycleData> getCycleDataList() {
        return cycleDataList;
    }

    public interface OnScrollListener {
        public void onScrolled(boolean isScrolling);
    }
}
