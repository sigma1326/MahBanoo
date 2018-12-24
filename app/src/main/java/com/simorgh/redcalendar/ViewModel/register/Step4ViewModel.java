package com.simorgh.redcalendar.ViewModel.register;

import androidx.lifecycle.ViewModel;

public class Step4ViewModel extends ViewModel {
    private int yellowDayCount = 3;

    public int getYellowDayCount() {
        return yellowDayCount;
    }

    public void setYellowDayCount(int yellowDayCount) {
        this.yellowDayCount = yellowDayCount;
    }
}
