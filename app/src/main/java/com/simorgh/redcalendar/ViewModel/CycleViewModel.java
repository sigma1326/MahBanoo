package com.simorgh.redcalendar.ViewModel;


import android.app.Application;

import com.simorgh.redcalendar.Model.database.CycleRepository;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.Model.database.model.DayMood;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CycleViewModel extends AndroidViewModel {
    private CycleRepository cycleRepository;
    private LiveData<Cycle> cycleLiveData;
    private int selectedDay = -1;


    public CycleViewModel(@NonNull Application application) {
        super(application);
        cycleRepository = new CycleRepository(application);
        cycleLiveData = cycleRepository.getCycleLiveData();
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
}
