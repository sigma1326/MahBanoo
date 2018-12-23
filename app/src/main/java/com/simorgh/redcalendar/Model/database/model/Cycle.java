package com.simorgh.redcalendar.Model.database.model;

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

    @NonNull
    @Override
    public String toString() {
        return "cycle: {" + redDaysCount + " : " + grayDaysCount + " : " + yellowDaysCount + " : " + startDate.getTime().toString() + "}";
    }
}
