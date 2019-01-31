package com.simorgh.mahbanoo.ViewModel.main;


import android.app.Application;

import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.databaseutils.model.User;
import com.simorgh.databaseutils.model.UserWithCycles;
import com.simorgh.mahbanoo.Model.AppManager;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CycleViewModel extends AndroidViewModel {
    private CycleRepository cycleRepository;
    private LiveData<UserWithCycles> userWithCyclesLiveData;
    private LiveData<List<DayMood>> moodsLiveData;
    private Cycle cycle;
    private User user;
    private int selectedDay = -1;
    private Calendar selectedDate = AppManager.getCalendarInstance();
    private Calendar selectedDateCalendar = AppManager.getCalendarInstance();
    private Calendar selectedDateMood = AppManager.getCalendarInstance();
    private Calendar selectedStartDate = AppManager.getCalendarInstance();


    public CycleViewModel(@NonNull Application application) {
        super(application);
        cycleRepository = AppManager.getCycleRepository();
        userWithCyclesLiveData = cycleRepository.getUserWithCyclesLiveData();
        moodsLiveData = cycleRepository.getMoodsLiveData();
    }

//    public DayMood getDayMood() {
//        return dayMood;
//    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public LiveData<UserWithCycles> getUserWithCyclesLiveData() {
        return userWithCyclesLiveData;
    }

    public void updateCycle(Cycle cycle) {
        cycleRepository.insertCycle(cycle);
    }

    public void updateUser(User user) {
        cycleRepository.insertUser(user);
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
