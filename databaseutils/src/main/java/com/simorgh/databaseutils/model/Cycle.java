package com.simorgh.databaseutils.model;

import com.simorgh.calendarutil.CalendarTool;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cycle")
public class Cycle {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cycle_id")
    private int cycleID;

    @ColumnInfo(name = "red_days_count")
    private int redDaysCount;

    @ColumnInfo(name = "gray_days_count")
    private int grayDaysCount;

    @ColumnInfo(name = "yellow_days_count")
    private int yellowDaysCount;

    @ColumnInfo(name = "start_date")
    private Calendar startDate;

    @ColumnInfo(name = "birth_year")
    private int birthYear;

    @ColumnInfo(name = "show_cycle_days")
    private boolean showCycleDays;

    @ColumnInfo(name = "show_pregnancy_prob")
    private boolean showPregnancyProb;

    public boolean isShowPregnancyProb() {
        return showPregnancyProb;
    }

    public void setShowPregnancyProb(boolean showPregnancyProb) {
        this.showPregnancyProb = showPregnancyProb;
    }

    public boolean isShowCycleDays() {
        return showCycleDays;
    }

    public void setShowCycleDays(boolean showCycleDays) {
        this.showCycleDays = showCycleDays;
    }

    public Cycle() {
        showPregnancyProb = true;
        showCycleDays = true;
    }

    public int getTotalDaysCount() {
        return redDaysCount + grayDaysCount;
    }

    public int getCycleID() {
        return cycleID;
    }

    public void setCycleID(int cycleID) {
        this.cycleID = cycleID;
    }

    public int getRedDaysCount() {
        return redDaysCount;
    }

    public void setRedDaysCount(int redDaysCount) {
        this.redDaysCount = redDaysCount;
    }

    public int getGrayDaysCount() {
        return grayDaysCount;
    }

    public void setGrayDaysCount(int grayDaysCount) {
        this.grayDaysCount = grayDaysCount;
    }

    public int getYellowDaysCount() {
        return yellowDaysCount;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setYellowDaysCount(int yellowDaysCount) {
        this.yellowDaysCount = yellowDaysCount;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    @NonNull
    @Override
    public String toString() {
        return "cycle: {" + redDaysCount + " : " + grayDaysCount + " : " + yellowDaysCount + " : "
                + birthYear + " : " + showCycleDays + " : " + showPregnancyProb + " : " + startDate.getTime().toString() + "}";
    }

    public Calendar getCurrentCycleStart(Calendar today) {
        Calendar calendar = Calendar.getInstance();
        long diffDays;
        int day2;
        diffDays = CalendarTool.getDaysFromDiff(today, getStartDate());
        if (diffDays >= 0) {
            day2 = (int) ((diffDays) % getTotalDaysCount()) + 1;
            calendar.add(Calendar.DAY_OF_MONTH, -1 * day2 + 1);
        } else {
            throw new UnsupportedOperationException("invalid cycle date");
        }
        return calendar;
    }
}