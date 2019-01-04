package com.simorgh.databaseutils.model;

import com.simorgh.calendarutil.CalendarTool;

import java.util.Calendar;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "cycles")
public class Cycle {
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "red_days_count")
    private int redDaysCount;

    @ColumnInfo(name = "gray_days_count")
    private int grayDaysCount;

    @ColumnInfo(name = "yellow_days_count")
    private int yellowDaysCount;

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "start_date")
    private Calendar startDate;

    @ColumnInfo(name = "end_date")
    private Calendar endDate;


    public int getTotalDaysCount() {
        return redDaysCount + grayDaysCount;
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

    public void setStartDate(@NonNull Calendar startDate) {
        if (this.startDate == null) {
            this.startDate = Calendar.getInstance();
            this.startDate.clear();
        }
        this.startDate.setTimeInMillis(startDate.getTimeInMillis());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setYellowDaysCount(int yellowDaysCount) {
        this.yellowDaysCount = yellowDaysCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "cycle: {" + redDaysCount + " : " + grayDaysCount + " : " + yellowDaysCount + " : " + startDate.getTime().toString() + " : " + endDate.getTime().toString() + "}";
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        if (endDate == null) {
            this.endDate = Calendar.getInstance();
            this.endDate.clear();
        } else {
            this.endDate = Calendar.getInstance();
            this.endDate.clear();
            this.endDate.setTimeInMillis(endDate.getTimeInMillis());
        }
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

    @Override
    public Cycle clone() {
        Cycle cycle = new Cycle();
        cycle.setUserId(getUserId());
        cycle.setStartDate(getStartDate());
        cycle.setEndDate(getEndDate());
        cycle.setGrayDaysCount(getGrayDaysCount());
        cycle.setRedDaysCount(getRedDaysCount());
        cycle.setYellowDaysCount(getYellowDaysCount());
        return cycle;
    }
}
