package com.simorgh.redcalendar.ViewModel.register;

import androidx.lifecycle.ViewModel;

public class Step3ViewModel extends ViewModel {
    private int grayDayCount = 24;

    public int getGrayDayCount() {
        return grayDayCount;
    }

    public void setGrayDayCount(int grayDayCount) {
        this.grayDayCount = grayDayCount;
    }
}
