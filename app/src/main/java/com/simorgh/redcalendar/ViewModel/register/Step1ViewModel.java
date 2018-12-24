package com.simorgh.redcalendar.ViewModel.register;

import java.util.Calendar;

import androidx.lifecycle.ViewModel;

public class Step1ViewModel extends ViewModel {
    public Step1ViewModel() {
        day = Calendar.getInstance();
    }

    private Calendar day;

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day.setTimeInMillis(day.getTimeInMillis());
    }
}
