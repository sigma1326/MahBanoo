package com.simorgh.redcalendar.ViewModel.register;

import androidx.lifecycle.ViewModel;

public class Step2ViewModel extends ViewModel {
    private int redDayCount = 4;

    public int getRedDayCount() {
        return redDayCount;
    }

    public void setRedDayCount(int redDayCount) {
        this.redDayCount = redDayCount;
    }
}
