package com.simorgh.cycleutils;

import android.util.Log;

import com.simorgh.calendarutil.CalendarTool;

import java.util.Calendar;

public class CycleData {
    public static final String TAG = "clueData";
    private static final int DEFAULT_RED_COUNT = 4;
    private static final int DEFAULT_GRAY_COUNT = 24;
    private static final int DEFAULT_GREEN_COUNT = 3;
    private static final int DEFAULT_YELLOW_COUNT = 3;
    private static final int DEFAULT_GREEN2_INDEX = 7;

    private int redCount = DEFAULT_RED_COUNT;
    private int grayCount = DEFAULT_GRAY_COUNT;
    private int greenStartIndex = DEFAULT_GREEN2_INDEX - 3;
    private int greenEndIndex = DEFAULT_GREEN2_INDEX + 3;
    private int yellowCount = DEFAULT_YELLOW_COUNT;
    private int yellowStartIndex = -1;
    private int yellowEndIndex = -1;
    private int green2Index = DEFAULT_GREEN2_INDEX;
    private int totalDays = 0;
    private Calendar startDate;
    private Calendar endDate;

    public CycleData(int redCount, int grayCount, int yellowCount, Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.redCount = redCount;
        this.grayCount = grayCount;
        this.yellowCount = yellowCount;
        this.totalDays = redCount + grayCount;
        switch (grayCount) {
            case 21:
                green2Index = redCount + 7;
                break;
            case 22:
                green2Index = redCount + 8;
                break;
            case 23:
                green2Index = redCount + 9;
                break;
            case 24:
                green2Index = redCount + 10;
                break;
            case 25:
                green2Index = redCount + 11;
                break;
            case 26:
                green2Index = redCount + 12;
                break;
            case 27:
                green2Index = redCount + 13;
                break;
            case 28:
                green2Index = redCount + 14;
                break;
            default:
                Log.d(TAG, "CycleData: invalid cycle length ");
        }
        greenStartIndex = green2Index - 3;
        greenEndIndex = green2Index + 3;
        yellowStartIndex = totalDays - yellowCount + 1;
        yellowEndIndex = totalDays;
    }

    public CycleData(int redCount, int grayCount, Calendar startDate) {
        this.redCount = redCount;
        this.grayCount = grayCount;
        this.yellowCount = DEFAULT_YELLOW_COUNT;
        this.totalDays = redCount + grayCount;
        switch (grayCount) {
            case 21:
                green2Index = redCount + 7;
                break;
            case 22:
                green2Index = redCount + 8;
                break;
            case 23:
                green2Index = redCount + 9;
                break;
            case 24:
                green2Index = redCount + 10;
                break;
            case 25:
                green2Index = redCount + 11;
                break;
            case 26:
                green2Index = redCount + 12;
                break;
            case 27:
                green2Index = redCount + 13;
                break;
            case 28:
                green2Index = redCount + 14;
                break;
            default:
                Log.d(TAG, "CycleData: invalid cycle length ");
        }
        greenStartIndex = green2Index - 3;
        greenEndIndex = green2Index + 3;
        yellowStartIndex = totalDays - yellowCount + 1;
        yellowEndIndex = totalDays;
    }

    public CycleData() {
    }

    public int getRedCount() {
        return redCount;
    }

    public void setRedCount(int redCount) {
        this.redCount = redCount;
    }

    public int getGrayCount() {
        return grayCount;
    }

    public void setGrayCount(int grayCount) {
        this.grayCount = grayCount;
    }

    public int getYellowCount() {
        return yellowCount;
    }

    public void setYellowCount(int yellowCount) {
        this.yellowCount = yellowCount;
    }

    public int getGreen2Index() {
        return green2Index;
    }

    public void setGreen2Index(int green2Index) {
        this.green2Index = green2Index;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getGreenStartIndex() {
        return greenStartIndex;
    }

    public void setGreenStartIndex(int greenStartIndex) {
        this.greenStartIndex = greenStartIndex;
    }

    public int getGreenEndIndex() {
        return greenEndIndex;
    }

    public void setGreenEndIndex(int greenEndIndex) {
        this.greenEndIndex = greenEndIndex;
    }

    public int getYellowStartIndex() {
        return yellowStartIndex;
    }

    public void setYellowStartIndex(int yellowStartIndex) {
        this.yellowStartIndex = yellowStartIndex;
    }

    public int getYellowEndIndex() {
        return yellowEndIndex;
    }

    public void setYellowEndIndex(int yellowEndIndex) {
        this.yellowEndIndex = yellowEndIndex;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        if (this.endDate == null) {
            this.endDate = Calendar.getInstance();
            this.endDate.clear();
        }
        this.endDate.setTimeInMillis(endDate.getTimeInMillis());
    }

    public void setStartDate(Calendar startDate) {
        if (this.startDate == null) {
            this.startDate = Calendar.getInstance();
            this.startDate.clear();
        }
        this.startDate.setTimeInMillis(startDate.getTimeInMillis());
    }

    private Calendar currentCycleStartDate = Calendar.getInstance();

    public Calendar getCurrentCycleStart(Calendar today) {
        long diffDays;
        int day2;
        diffDays = CalendarTool.getDaysFromDiff(today, startDate);
        if (diffDays >= 0) {
            day2 = (int) ((diffDays) % getTotalDays()) + 1;
            currentCycleStartDate.clear();
            currentCycleStartDate.setTimeInMillis(today.getTimeInMillis());
            currentCycleStartDate.add(Calendar.DAY_OF_MONTH, -1 * day2 + 1);
        } else {
            Log.d("debug13", "invalid cycle date");
        }
        return currentCycleStartDate;
    }
}
