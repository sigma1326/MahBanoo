package com.simorgh.redcalendar.ViewModel.main;


import android.app.Application;

import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CycleViewModel extends AndroidViewModel {
    private CycleRepository cycleRepository;
    private LiveData<Cycle> cycleLiveData;
    private LiveData<List<DayMood>> moodsLiveData;
    private Cycle cycle;
    private int selectedDay = -1;
    private Calendar selectedDate = AppManager.getCalendarInstance();
    private Calendar selectedDateCalendar = AppManager.getCalendarInstance();
    private Calendar selectedDateMood = AppManager.getCalendarInstance();
    private Calendar selectedStartDate = AppManager.getCalendarInstance();


    public CycleViewModel(@NonNull Application application) {
        super(application);
        cycleRepository = new CycleRepository(application);
        cycleLiveData = cycleRepository.getCycleLiveData();
        moodsLiveData = cycleRepository.getListLiveData();
    }

//    public DayMood getDayMood() {
//        return dayMood;
//    }

    public LiveData<List<DayMood>> getMoodsLiveData() {
        return moodsLiveData;
    }

    public void setMoodsLiveData(LiveData<List<DayMood>> moodsLiveData) {
        this.moodsLiveData = moodsLiveData;
    }

    public Calendar getSelectedDateCalendar() {
        return selectedDateCalendar;
    }

    public void setSelectedDateCalendar(Calendar selectedDateCalendar) {
        this.selectedDateCalendar.setTimeInMillis(selectedDateCalendar.getTimeInMillis());
    }

    public Calendar getSelectedDateMood() {
        return selectedDateMood;
    }

    public DayMood getSelectedDateMood(Calendar day) {
        return cycleRepository.getDayMood(day);
    }

    public void setSelectedDateMood(Calendar selectedDateMood) {
        this.selectedDateMood.setTimeInMillis(selectedDateMood.getTimeInMillis());
    }

    public DayMood getDayMood() {
        return cycleRepository.getDayMood(selectedDateMood);
    }

    public DayMood getDayMood(Calendar calendar) {
        return cycleRepository.getDayMood(calendar);
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public void setSelectedDay(int selectedDay) {
        this.selectedDay = selectedDay;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public LiveData<Cycle> getCycleLiveData() {
        return cycleLiveData;
    }

    public void updateCycle(Cycle cycle) {
        cycleRepository.insertCycle(cycle);
    }

    public void clearData() {
        cycleRepository.clearData();
    }

    public void updateDayMood(DayMood dayMood) {
        cycleRepository.insertDayMood(dayMood);
    }

    public Calendar getSelectedStartDate() {
        return selectedStartDate;
    }

    public void setSelectedStartDate(Calendar selectedStartDate) {
        this.selectedStartDate.setTimeInMillis(selectedStartDate.getTimeInMillis());
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate.setTimeInMillis(selectedDate.getTimeInMillis());
    }
}
