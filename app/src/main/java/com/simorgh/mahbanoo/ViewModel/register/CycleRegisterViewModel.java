package com.simorgh.mahbanoo.ViewModel.register;

import java.util.Calendar;

import androidx.lifecycle.ViewModel;

public class CycleRegisterViewModel extends ViewModel {
    private int redDaysCount;
    private int grayDaysCount;
    private int yellowDaysCount;
    private int birthYear;
    private Calendar lastCycleEndDay;

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

    public void setYellowDaysCount(int yellowDaysCount) {
        this.yellowDaysCount = yellowDaysCount;
    }

    public Calendar getLastCycleEndDay() {
        return lastCycleEndDay;
    }

    public void setLastCycleEndDay(Calendar lastCycleEndDay) {
        this.lastCycleEndDay = lastCycleEndDay;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
