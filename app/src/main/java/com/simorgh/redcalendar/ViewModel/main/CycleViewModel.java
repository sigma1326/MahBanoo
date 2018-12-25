package com.simorgh.redcalendar.ViewModel.main;


import android.app.Application;

import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.database.CycleRepository;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.Model.database.model.DayMood;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CycleViewModel extends AndroidViewModel {
    private CycleRepository cycleRepository;
    private LiveData<Cycle> cycleLiveData;
    private Cycle cycle;
    private int selectedDay = -1;
    private Calendar selectedDate = AppManager.getCalendarInstance();


    public CycleViewModel(@NonNull Application application) {
        super(application);
        cycleRepository = new CycleRepository(application);
        cycleLiveData = cycleRepository.getCycleLiveData();
//        cycle = cycleLiveData.getValue();
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

    public void insertDayMood(DayMood dayMood) {
        cycleRepository.insertDayMood(dayMood);
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate.setTimeInMillis(selectedDate.getTimeInMillis());
    }
}
