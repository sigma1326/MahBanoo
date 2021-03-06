/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simorgh.cyclecalendar.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.hijricalendar.UmmalquraCalendar;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cyclecalendar.view.ChangeDaysMonthView;
import com.simorgh.cyclecalendar.view.SetStartDayMonthView;
import com.simorgh.cyclecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.CycleData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * An adapter for a list of {@link ShowInfoMonthView} items.
 */
public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> implements
        BaseMonthView.OnDaySelectedListener, BaseMonthView.IsDaySelectedListener, BaseMonthView.IsDayInRangeSelectedListener, BaseMonthView.OnCycleDataListNeededListener {
    private static final int MONTHS_IN_YEAR = 12;

    private final Calendar mMinDate = Calendar.getInstance();
    private final Calendar mMaxDate = Calendar.getInstance();
    private static Calendar prevCycleRedEnd = Calendar.getInstance();

    private ArrayList<MonthViewHolder> mItems = new ArrayList<>();

    private int calendarType;
    private int monthViewType;


    private Calendar today = Calendar.getInstance();
    private Calendar selectedDay = Calendar.getInstance();

    private PersianCalendar minP;

    private UmmalquraCalendar minH;


    private int mCount;
    private int mFirstDayOfWeek;
    private ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener;
    private CycleData cycleData;
    private boolean showInfo;
    private List<CycleData> cycleDataList = new LinkedList<>();
    private Context context;

    private void init() {
        int diffYear = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        int diffMonth = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                break;
            case CalendarType.PERSIAN:
                PersianCalendar p1 = CalendarTool.GregorianToPersian(mMinDate);
                PersianCalendar p2 = CalendarTool.GregorianToPersian(mMaxDate);
                diffYear = p2.getPersianYear() - p1.getPersianYear();
                diffMonth = p2.getPersianMonth() - p1.getPersianMonth();
                break;
            case CalendarType.ARABIC:
                UmmalquraCalendar h1 = CalendarTool.GregorianToHijri(mMinDate);
                UmmalquraCalendar h2 = CalendarTool.GregorianToHijri(mMaxDate);
                diffYear = h2.get(UmmalquraCalendar.YEAR) - h1.get(UmmalquraCalendar.YEAR);
                diffMonth = h2.get(UmmalquraCalendar.MONTH) - h1.get(UmmalquraCalendar.MONTH);
                break;
        }
        mCount = diffMonth + MONTHS_IN_YEAR * diffYear + 1;
    }

    public MonthViewAdapter(@NonNull Context context, int calendarType, int monthViewType) {
        this.context = context;
        this.calendarType = calendarType;
        this.monthViewType = monthViewType;


        mMinDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        mMaxDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis());

        init();
    }

    public Calendar getSelectedDay() {
        return selectedDay;
    }

    public void setSelectedDay(Calendar selectedDay) {
        this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
        notifyDataSetChanged();
    }

    public void setRange(@NonNull Calendar min, @NonNull Calendar max) {
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());

        init();

        // Positions are selectedDay invalid, clear everything and start over.
        notifyDataSetChanged();
    }

    public void setFirstDayOfWeek(int weekStart) {
        mFirstDayOfWeek = weekStart;
        // Update displayed views.
        final int count = mItems.size();
        for (int i = 0; i < count; i++) {
            final ShowInfoMonthView showInfoMonthView = (ShowInfoMonthView) mItems.get(i).itemView;
            showInfoMonthView.setFirstDayOfWeek(weekStart);
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }


    private int getMonthForPosition(int position) {
        int ret = 0;
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                ret = (position + mMinDate.get(Calendar.MONTH)) % MONTHS_IN_YEAR;
                break;
            case CalendarType.ARABIC:
                minH = CalendarTool.GregorianToHijri(mMinDate);
                ret = (position + minH.get(UmmalquraCalendar.MONTH)) % MONTHS_IN_YEAR;
                break;
            case CalendarType.PERSIAN:
                minP = CalendarTool.GregorianToPersian(mMinDate);
                ret = (position + minP.getPersianMonth()) % MONTHS_IN_YEAR;
                break;
        }

        return ret;
    }

    private int getYearForPosition(int position) {
        int yearOffset;
        int ret = 0;
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                yearOffset = (position + mMinDate.get(Calendar.MONTH)) / MONTHS_IN_YEAR;
                ret = yearOffset + mMinDate.get(Calendar.YEAR);
                break;
            case CalendarType.ARABIC:
                minH = CalendarTool.GregorianToHijri(mMinDate);
                yearOffset = (position + minH.get(UmmalquraCalendar.MONTH)) / MONTHS_IN_YEAR;
                ret = yearOffset + minH.get(UmmalquraCalendar.YEAR);
                break;
            case CalendarType.PERSIAN:
                minP = CalendarTool.GregorianToPersian(mMinDate);
                yearOffset = (position + minP.getPersianMonth()) / MONTHS_IN_YEAR;
                ret = yearOffset + minP.getPersianYear();
                break;
        }
        return ret;
    }

    private ShowInfoMonthView.OnDayClickListener onDayClickListener;

    public ShowInfoMonthView.OnDayClickListener getOnDayClickListener() {
        return onDayClickListener;
    }

    public void setOnDayClickListener(ShowInfoMonthView.OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
        notifyDataSetChanged();
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
        notifyDataSetChanged();
    }

    public int getCalendarType() {
        return calendarType;
    }

    public int getMonthViewType() {
        return monthViewType;
    }

    public void setMonthViewType(int monthViewType) {
        this.monthViewType = monthViewType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseMonthView v;
        switch (monthViewType) {
            case BaseMonthView.MonthViewTypeChangeDays:
                v = new ChangeDaysMonthView(parent.getContext());
                break;
            case BaseMonthView.MonthViewTypeShowCalendar:
                v = new ShowInfoMonthView(parent.getContext());
                ((ShowInfoMonthView) v).setIsDayMarkedListener(isDayMarkedListener);
                break;
            case BaseMonthView.MonthViewTypeSetStartDay:
                v = new SetStartDayMonthView(parent.getContext());
                break;
            default:
                v = new ShowInfoMonthView(parent.getContext());
        }
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setPadding(4, 4, 4, 4);
        MonthViewHolder holder = new MonthViewHolder(0, v);
        ((BaseMonthView) holder.itemView).setCycleData(cycleData);
        ((BaseMonthView) holder.itemView).setOnDayClickListener(onDayClickListener);
        ((BaseMonthView) holder.itemView).setOnDaySelectedListener(this);
        ((BaseMonthView) holder.itemView).setIsDaySelectedListener(this);
        ((BaseMonthView) holder.itemView).setIsDayInRangeSelectedListener(this);
        ((BaseMonthView) holder.itemView).setOnCycleDataListNeededListener(this);
        mItems.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        holder.position = position;
//        Log.d(TAG, "onBindViewHolder: " + position);
        ((BaseMonthView) holder.itemView).setOnDayClickListener(onDayClickListener);

        final int month = getMonthForPosition(position);
        final int year = getYearForPosition(position);

        int selectedDay = 0;
        int enabledDayRangeStart = 0;
        int enabledDayRangeEnd = 0;

        switch (calendarType) {
            case CalendarType.GREGORIAN:
                if (today != null && today.get(Calendar.MONTH) == month) {
                    selectedDay = today.get(Calendar.DAY_OF_MONTH);
                } else {
                    selectedDay = -1;
                }

                if (mMinDate.get(Calendar.MONTH) == month && mMinDate.get(Calendar.YEAR) == year) {
                    enabledDayRangeStart = mMinDate.get(Calendar.DAY_OF_MONTH);
                } else {
                    enabledDayRangeStart = 1;
                }

                if (mMaxDate.get(Calendar.MONTH) == month && mMaxDate.get(Calendar.YEAR) == year) {
                    enabledDayRangeEnd = mMaxDate.get(Calendar.DAY_OF_MONTH);
                } else {
                    enabledDayRangeEnd = 31;
                }
                break;
            case CalendarType.PERSIAN:
                PersianCalendar p = CalendarTool.GregorianToPersian(today);
                if (today != null && p.getPersianMonth() == month) {
                    selectedDay = p.getPersianDay();
                } else {
                    selectedDay = -1;
                }

                p = CalendarTool.GregorianToPersian(mMinDate);
                if (p.getPersianMonth() == month && p.getPersianYear() == year) {
                    enabledDayRangeStart = p.getPersianDay();
                } else {
                    enabledDayRangeStart = 1;
                }

                p = CalendarTool.GregorianToPersian(mMaxDate);
                if (p.getPersianMonth() == month && p.getPersianYear() == year) {
                    enabledDayRangeEnd = p.getPersianDay();
                } else {
                    enabledDayRangeEnd = 31;
                }
                break;
            case CalendarType.ARABIC:
                UmmalquraCalendar hijriCalendar = CalendarTool.GregorianToHijri(today);
                if (today != null && hijriCalendar.get(UmmalquraCalendar.MONTH) == month) {
                    selectedDay = hijriCalendar.get(UmmalquraCalendar.DAY_OF_MONTH);
                } else {
                    selectedDay = -1;
                }

                hijriCalendar = CalendarTool.GregorianToHijri(mMinDate);
                if (hijriCalendar.get(UmmalquraCalendar.MONTH) == month && hijriCalendar.get(UmmalquraCalendar.YEAR) == year) {
                    enabledDayRangeStart = hijriCalendar.get(UmmalquraCalendar.DAY_OF_MONTH);
                } else {
                    enabledDayRangeStart = 1;
                }

                hijriCalendar = CalendarTool.GregorianToHijri(mMaxDate);
                if (hijriCalendar.get(UmmalquraCalendar.MONTH) == month && hijriCalendar.get(UmmalquraCalendar.YEAR) == year) {
                    enabledDayRangeEnd = hijriCalendar.get(UmmalquraCalendar.DAY_OF_MONTH);
                } else {
                    enabledDayRangeEnd = 31;
                }
                break;
        }
        switch (monthViewType) {
            case BaseMonthView.MonthViewTypeChangeDays:
                break;
            case BaseMonthView.MonthViewTypeShowCalendar:
                ((ShowInfoMonthView) holder.itemView).setIsDayMarkedListener(isDayMarkedListener);
                break;
            case BaseMonthView.MonthViewTypeSetStartDay:
                break;
            default:
        }
        ((BaseMonthView) holder.itemView).setMonthParams(selectedDay, month, year, mFirstDayOfWeek
                , enabledDayRangeStart, enabledDayRangeEnd, calendarType, showInfo);
    }

    @Override
    public void onViewRecycled(@NonNull MonthViewHolder holder) {
        if (holder.position < mItems.size())
            mItems.remove(holder.position);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull MonthViewHolder holder) {
//        int position = holder.position;
//        Log.d(TAG, "onViewAttachedToWindow: " + position);
//        Log.d(TAG, "onViewAttachedToWindow: " + position + " = " + month + " :: " + year + " :: " + enabledDayRangeStart + " :: " + enabledDayRangeEnd);

    }

    public ShowInfoMonthView.IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MonthViewHolder holder) {
        if (holder.position < mItems.size()) {
            mItems.remove(holder.position);
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public MonthViewAdapter(Context context, CycleData cycleData, int calendarType, int monthViewType, Calendar min, Calendar max) {
        this.context = context;
        this.calendarType = calendarType;
        this.monthViewType = monthViewType;
        this.cycleData = cycleData;
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());
        init();
    }

    @Override
    public void onDaySelected(Calendar selectedDay) {
        boolean changed = false;
        if (monthViewType == BaseMonthView.MonthViewTypeSetStartDay) {
            if (!selectedDay.after(Calendar.getInstance())) {
                this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
                changed = true;
                notifyDataSetChanged();
            }
        } else if (monthViewType == BaseMonthView.MonthViewTypeChangeDays) {
            if (CalendarTool.getDaysFromDiff(selectedDay, today) <= 0) {
                if (cycleDataList != null && !cycleDataList.isEmpty()) {
                    int size = cycleDataList.size();
                    CycleData prevCycle = cycleDataList.size() > 1 ? cycleDataList.get(size - 1) : cycleDataList.get(0);
                    prevCycleRedEnd.clear();
                    prevCycleRedEnd.setTimeInMillis(prevCycle.getStartDate().getTimeInMillis());
                    prevCycleRedEnd.add(Calendar.DAY_OF_MONTH, prevCycle.getRedCount());
                    if (CalendarTool.getDaysFromDiff(prevCycleRedEnd, selectedDay) <= 0) {
                        this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
                        changed = true;
                        notifyDataSetChanged();
                    }
                } else {
                    this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
                    changed = true;
                    notifyDataSetChanged();
                }
            }
        } else {
            if (CalendarTool.getDaysFromDiff(selectedDay, today) <= 0) {
                this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
                changed = true;
                notifyDataSetChanged();
            }
        }
        if (!changed && context != null) {
            Toast.makeText(context, "قابل انتخاب نیست", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isDaySelected(Calendar day) {
        return selectedDay.get(Calendar.YEAR) == day.get(Calendar.YEAR) && selectedDay.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                && selectedDay.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean isDayInRangeSelected(Calendar day, CycleData cycleData) {
        if (cycleData == null) {
            return false;
        }
        boolean firstDay = selectedDay.get(Calendar.YEAR) == day.get(Calendar.YEAR) && selectedDay.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                && selectedDay.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
        long diff = CalendarTool.getDaysFromDiff(day, selectedDay);
        if (diff < 0) {
            return firstDay;
        } else {
            return firstDay || diff < cycleData.getRedCount();
        }
    }

    public void setCycleData(CycleData cycleData) {
        this.cycleData = cycleData;
        for (int i = 0; i < mItems.size(); i++) {
            ((BaseMonthView) mItems.get(i).itemView).setCycleData(cycleData);
            notifyItemChanged(i);
        }
    }

    public CycleData getCycleData() {
        return cycleData;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
        notifyDataSetChanged();
    }

    public boolean getShowInfo() {
        return showInfo;
    }

    public void setCycleDataList(List<CycleData> cycleDataList) {
        if (this.cycleDataList == null) {
            this.cycleDataList = new LinkedList<>();
        }
        if (cycleDataList != null) {
            this.cycleDataList.addAll(cycleDataList);
            for (int i = 0; i < mItems.size(); i++) {
                ((BaseMonthView) mItems.get(i).itemView).setCycleDataList(cycleDataList);
                notifyItemChanged(i);
            }
        }
    }

    public List<CycleData> getCycleDataList() {
        return cycleDataList;
    }

    @Override
    public List<CycleData> onCycleDataNeeded() {
        return cycleDataList;
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        public int position;

        public MonthViewHolder(int position, BaseMonthView baseMonthView) {
            super(baseMonthView);
            this.position = position;
        }
    }
}
