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
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.MonthView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


/**
 * An adapter for a list of {@link com.simorgh.cluecalendar.view.MonthView} items.
 */
public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> {
    private static final int MONTHS_IN_YEAR = 12;

    private final Calendar mMinDate = Calendar.getInstance();
    private final Calendar mMaxDate = Calendar.getInstance();

    private ArrayList<MonthViewHolder> mItems = new ArrayList<>();

    private int calendarType;

    private Calendar mSelectedDay = Calendar.getInstance();
    private PersianCalendar persianCalendar;
    private PersianCalendar minP;
    private PersianCalendar p1;
    private PersianCalendar p2;
    private UmmalquraCalendar hijriCalendar;
    private UmmalquraCalendar minH;
    private UmmalquraCalendar h1;
    private UmmalquraCalendar h2;


    private OnDaySelectedListener mOnDaySelectedListener;

    private int mCount;
    private int mFirstDayOfWeek;

    private ColorStateList fixedDayTextColors;
    private Context mContext;

    public MonthViewAdapter(@NonNull Context context, int calendarType) {
        this.mContext = context;
        this.calendarType = calendarType;

        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, 2016);
        maxDate.set(Calendar.YEAR, 2030);
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

    /**
     * Sets the first day of the week.
     *
     * @param weekStart which day the week should start on, valid values are
     *                  {@link Calendar#SUNDAY} through {@link Calendar#SATURDAY}
     */
    public void setFirstDayOfWeek(int weekStart) {
        mFirstDayOfWeek = weekStart;
        // Update displayed views.
        final int count = mItems.size();
        for (int i = 0; i < count; i++) {
            final MonthView monthView = mItems.get(i).monthView;
            monthView.setFirstDayOfWeek(weekStart);
        }
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener listener) {
        mOnDaySelectedListener = listener;
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


    MonthView getView(Object object) {
        if (object == null) {
            return null;
        }
        final ViewHolder holder = (ViewHolder) object;
        return holder.monthView;
    }

    private final MonthView.OnDayClickListener mOnDayClickListener = new MonthView.OnDayClickListener() {
        @Override
        public void onDayClick(BaseMonthView view, Calendar day) {
            if (day != null) {
                if (mOnDaySelectedListener != null) {
                    mOnDaySelectedListener.onDaySelected(MonthViewAdapter.this, day);
                }
            }
        }
    };

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
    }

    public int getCalendarType() {
        return calendarType;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MonthView v = new MonthView(parent.getContext());
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setPadding(4, 0, 4, 0);
        MonthViewHolder holder = new MonthViewHolder(-1, parent, v);
        mItems.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        holder.position = position;
        if (position >= mItems.size()) {
            return;
        }
        final MonthView v = mItems.get(position).monthView;
        v.setOnDayClickListener(mOnDayClickListener);
        final int month = getMonthForPosition(position);
        final int year = getYearForPosition(position);
//        Log.d("d13", "onBindViewHolder: " + position + " = " + month + " :: " + year);

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

        v.setMonthParams(selectedDay, month, year, mFirstDayOfWeek, enabledDayRangeStart, enabledDayRangeEnd, calendarType);
//        v.setFirstDayOfWeek(Calendar.SATURDAY);
    }

    @Override
    public void onViewRecycled(@NonNull MonthViewHolder holder) {
        if (holder.position < mItems.size())
            mItems.remove(holder.position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MonthViewHolder holder) {
//        Log.d("d13", "onViewAttachedToWindow: " + holder.position);
//        mItems.add(holder);
        int position = holder.position;
        super.onViewAttachedToWindow(holder);
        final int month = getMonthForPosition(position);
        final int year = getYearForPosition(position);
        holder.monthView.setMonthParams(-1, month, year, mFirstDayOfWeek, 1, 31, calendarType);

        Log.d("d13", "onViewAttachedToWindow: " + position + " = " + month + " :: " + year);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MonthViewHolder holder) {
//        Log.d("d13", "onViewDetachedFromWindow: " + holder.position);
//        if (holder.position < mItems.size())
//            mItems.remove(holder.position);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private static class ViewHolder {
        public final int position;
        public final View container;
        public final MonthView monthView;

        public ViewHolder(int position, View container, MonthView monthView) {
            this.position = position;
            this.container = container;
            this.monthView = monthView;
        }
    }

    public interface OnDaySelectedListener {
        public void onDaySelected(MonthViewAdapter monthViewAdapter, Calendar day);
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder {
        public int position;
        public final View container;
        public final MonthView monthView;

        public MonthViewHolder(int position, View container, MonthView monthView) {
            super(monthView);
            this.position = position;
            this.container = container;
            this.monthView = monthView;
        }
    }
}
