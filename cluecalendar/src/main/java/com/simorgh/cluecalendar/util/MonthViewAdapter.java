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

package com.simorgh.cluecalendar.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.ChangeDaysMonthView;
import com.simorgh.cluecalendar.view.SetStartDayMonthView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.simorgh.cluecalendar.view.BaseMonthView.TAG;


/**
 * An adapter for a list of {@link ShowInfoMonthView} items.
 */
public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> implements
        BaseMonthView.OnDaySelectedListener, BaseMonthView.IsDaySelectedListener, BaseMonthView.IsDayInRangeSelectedListener {
    private static final int MONTHS_IN_YEAR = 12;

    private final Calendar mMinDate = Calendar.getInstance();
    private final Calendar mMaxDate = Calendar.getInstance();

    private ArrayList<MonthViewHolder> mItems = new ArrayList<>();

    private int calendarType;
    private int monthViewType;


    private Calendar mSelectedDay = Calendar.getInstance();
    private Calendar selectedDay = Calendar.getInstance();
    private PersianCalendar persianCalendar;
    private PersianCalendar minP;
    private PersianCalendar p1;
    private PersianCalendar p2;
    private UmmalquraCalendar hijriCalendar;
    private UmmalquraCalendar minH;
    private UmmalquraCalendar h1;
    private UmmalquraCalendar h2;


    private int mCount;
    private int mFirstDayOfWeek;
    private ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener;
    private int lastPosition = -1;
    private BaseMonthView.ClueData clueData;


    public MonthViewAdapter(@NonNull Context context, int calendarType, int monthViewType) {
        this.calendarType = calendarType;
        this.monthViewType = monthViewType;

        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        mMinDate.setTimeInMillis(minDate.getTimeInMillis());
        mMaxDate.setTimeInMillis(maxDate.getTimeInMillis());

        int diffYear = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        int diffMonth = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                break;
            case CalendarType.PERSIAN:
                p1 = CalendarTool.GregorianToPersian(mMinDate);
                p2 = CalendarTool.GregorianToPersian(mMaxDate);
                diffYear = p2.getPersianYear() - p1.getPersianYear();
                diffMonth = p2.getPersianMonth() - p1.getPersianMonth();
                break;
            case CalendarType.ARABIC:
                h1 = CalendarTool.GregorianToHijri(mMinDate);
                h2 = CalendarTool.GregorianToHijri(mMaxDate);
                diffYear = h2.get(UmmalquraCalendar.YEAR) - h1.get(UmmalquraCalendar.YEAR);
                diffMonth = h2.get(UmmalquraCalendar.MONTH) - h1.get(UmmalquraCalendar.MONTH);
                break;
        }
        mCount = diffMonth + MONTHS_IN_YEAR * diffYear + 1;
    }

    public void setRange(@NonNull Calendar min, @NonNull Calendar max) {
        mMinDate.setTimeInMillis(min.getTimeInMillis());
        mMaxDate.setTimeInMillis(max.getTimeInMillis());

        int diffYear = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
        int diffMonth = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                break;
            case CalendarType.PERSIAN:
                p1 = CalendarTool.GregorianToPersian(mMinDate);
                p2 = CalendarTool.GregorianToPersian(mMaxDate);
                diffYear = p2.getPersianYear() - p1.getPersianYear();
                diffMonth = p2.getPersianMonth() - p1.getPersianMonth();
                break;
            case CalendarType.ARABIC:
                h1 = CalendarTool.GregorianToHijri(mMinDate);
                h2 = CalendarTool.GregorianToHijri(mMaxDate);
                diffYear = h2.get(UmmalquraCalendar.YEAR) - h1.get(UmmalquraCalendar.YEAR);
                diffMonth = h2.get(UmmalquraCalendar.MONTH) - h1.get(UmmalquraCalendar.MONTH);
                break;
        }
        mCount = diffMonth + MONTHS_IN_YEAR * diffYear + 1;

        // Positions are now invalid, clear everything and start over.
        notifyDataSetChanged();
    }

    public void setFirstDayOfWeek(int weekStart) {
        mFirstDayOfWeek = weekStart;
        // Update displayed views.
        final int count = mItems.size();
        for (int i = 0; i < count; i++) {
            final ShowInfoMonthView showInfoMonthView = (ShowInfoMonthView) mItems.get(i).baseMonthView;
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

    private int getPositionForDay(@Nullable Calendar day) {
        if (day == null) {
            return -1;
        }
        final int yearOffset;
        final int monthOffset;
        int position = 0;
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                yearOffset = day.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
                monthOffset = day.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
                position = yearOffset * MONTHS_IN_YEAR + monthOffset;
                break;
            case CalendarType.ARABIC:
                hijriCalendar = CalendarTool.GregorianToHijri(day);
                minH = CalendarTool.GregorianToHijri(mMinDate);
                yearOffset = hijriCalendar.get(UmmalquraCalendar.YEAR) - minH.get(UmmalquraCalendar.YEAR);
                monthOffset = hijriCalendar.get(UmmalquraCalendar.MONTH) - minH.get(UmmalquraCalendar.MONTH);
                position = yearOffset * MONTHS_IN_YEAR + monthOffset;
                break;
            case CalendarType.PERSIAN:
                persianCalendar = CalendarTool.GregorianToPersian(day);
                minP = CalendarTool.GregorianToPersian(mMinDate);
                yearOffset = persianCalendar.getPersianYear() - minP.getPersianYear();
                monthOffset = persianCalendar.getPersianMonth() - minP.getPersianMonth();
                position = yearOffset * MONTHS_IN_YEAR + monthOffset;
                break;
        }

        return position;
    }


    ShowInfoMonthView getView(Object object) {
        if (object == null) {
            return null;
        }
        final ViewHolder holder = (ViewHolder) object;
        return holder.showInfoMonthView;
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
        MonthViewHolder holder = new MonthViewHolder(-1, parent, v);
        holder.baseMonthView.setClueData(clueData);
        holder.baseMonthView.setOnDayClickListener(onDayClickListener);
        holder.baseMonthView.setOnDaySelectedListener(this);
        holder.baseMonthView.setIsDaySelectedListener(this);
        holder.baseMonthView.setIsDayInRangeSelectedListener(this);
        mItems.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        holder.position = position;
    }

    @Override
    public void onViewRecycled(@NonNull MonthViewHolder holder) {
        if (holder.position < mItems.size())
            mItems.remove(holder.position);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull MonthViewHolder holder) {
        int position = holder.position;

        holder.baseMonthView.setOnDayClickListener(onDayClickListener);

        final int month = getMonthForPosition(position);
        final int year = getYearForPosition(position);

        int selectedDay = 0;
        int enabledDayRangeStart = 0;
        int enabledDayRangeEnd = 0;

        switch (calendarType) {
            case CalendarType.GREGORIAN:
                if (mSelectedDay != null && mSelectedDay.get(Calendar.MONTH) == month) {
                    selectedDay = mSelectedDay.get(Calendar.DAY_OF_MONTH);
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
                PersianCalendar p = CalendarTool.GregorianToPersian(mSelectedDay);
                if (mSelectedDay != null && p.getPersianMonth() == month) {
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
                UmmalquraCalendar hijriCalendar = CalendarTool.GregorianToHijri(mSelectedDay);
                if (mSelectedDay != null && hijriCalendar.get(UmmalquraCalendar.MONTH) == month) {
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
//        Log.d(TAG, "onViewAttachedToWindow: " + position + " = " + month + " :: " + year + " :: " + enabledDayRangeStart + " :: " + enabledDayRangeEnd);
        switch (monthViewType) {
            case BaseMonthView.MonthViewTypeChangeDays:
                break;
            case BaseMonthView.MonthViewTypeShowCalendar:
                ((ShowInfoMonthView) holder.baseMonthView).setIsDayMarkedListener(isDayMarkedListener);
                break;
            case BaseMonthView.MonthViewTypeSetStartDay:
                break;
            default:
        }
        holder.baseMonthView.setMonthParams(selectedDay, month, year, mFirstDayOfWeek, enabledDayRangeStart, enabledDayRangeEnd, calendarType);
//        mItems.add(holder);
//        onViewDetachedFromWindow(holder);
    }

    public ShowInfoMonthView.IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(ShowInfoMonthView.IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MonthViewHolder holder) {
//        Log.d("d13", "onViewDetachedFromWindow: " + holder.position);
        if (holder.position < mItems.size())
            mItems.remove(holder.position);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public void onDaySelected(Calendar selectedDay) {
        if (monthViewType == BaseMonthView.MonthViewTypeSetStartDay) {
            if (!selectedDay.after(Calendar.getInstance())) {
                this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
            }
        } else {
            this.selectedDay.setTimeInMillis(selectedDay.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    private int getPositionForMonth(Calendar selectedDay) {
        int position = 0;
        switch (calendarType) {
            case CalendarType.GREGORIAN:
                int diffYear = selectedDay.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR);
                int diffMonth = selectedDay.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH);
                position = diffYear * 12 + diffMonth;
                break;
            case CalendarType.PERSIAN:
                diffYear = CalendarTool.GregorianToPersian(selectedDay).getPersianYear() - CalendarTool.GregorianToPersian(mMinDate).getPersianYear();
                diffMonth = CalendarTool.GregorianToPersian(selectedDay).getPersianMonth() - CalendarTool.GregorianToPersian(mMinDate).getPersianMonth();
                position = diffYear * 12 + diffMonth;
                break;
            case CalendarType.ARABIC:
                diffYear = CalendarTool.GregorianToHijri(selectedDay).get(UmmalquraCalendar.YEAR) - CalendarTool.GregorianToHijri(mMinDate).get(UmmalquraCalendar.YEAR);
                diffMonth = CalendarTool.GregorianToHijri(selectedDay).get(UmmalquraCalendar.MONTH) - CalendarTool.GregorianToHijri(mMinDate).get(UmmalquraCalendar.MONTH);
                position = diffYear * 12 + diffMonth;
                break;
        }
        return position;
    }

    @Override
    public boolean isDaySelected(Calendar day) {
        return selectedDay.get(Calendar.YEAR) == day.get(Calendar.YEAR) && selectedDay.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                && selectedDay.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean isDayInRangeSelected(Calendar day, BaseMonthView.ClueData clueData) {
        boolean firstDay = selectedDay.get(Calendar.YEAR) == day.get(Calendar.YEAR) && selectedDay.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                && selectedDay.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
        long diff = CalendarTool.getDaysFromDiff(day, selectedDay);
        if (diff < 0) {
            return firstDay;
        } else {
            return firstDay || diff < clueData.getRedCount();
        }
    }

    public void setClueData(BaseMonthView.ClueData clueData) {
        this.clueData = clueData;
    }

    public BaseMonthView.ClueData getClueData() {
        return clueData;
    }

    private static class ViewHolder {
        public final int position;
        public final View container;
        public final ShowInfoMonthView showInfoMonthView;

        public ViewHolder(int position, View container, ShowInfoMonthView showInfoMonthView) {
            this.position = position;
            this.container = container;
            this.showInfoMonthView = showInfoMonthView;
        }
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        public int position;
        public final View container;
        public final BaseMonthView baseMonthView;

        public MonthViewHolder(int position, View container, BaseMonthView baseMonthView) {
            super(baseMonthView);
            this.position = position;
            this.container = container;
            this.baseMonthView = baseMonthView;
        }
    }
}
