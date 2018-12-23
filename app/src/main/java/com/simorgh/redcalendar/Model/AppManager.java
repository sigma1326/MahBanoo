package com.simorgh.redcalendar.Model;

import android.app.Application;

import com.simorgh.cluecalendar.util.ClueData;
import com.simorgh.clueview.ClueView;
import com.simorgh.redcalendar.Model.database.CycleRepository;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.ViewModel.CycleViewModel;

import java.util.Calendar;

import androidx.lifecycle.ViewModelProviders;

public class AppManager extends Application {
    public static final String DB_NAME = "red-calendar-db";
    public static final String TAG = "debug13";
    public static ClueView.ClueData cvClueData;
    public static com.simorgh.cyclebar.ClueData cbClueData;
    public static ClueData clueData;
    public static com.simorgh.weekdaypicker.ClueData wdpClueData;
    public static Calendar minDate;
    public static Calendar maxDate;
    public static Calendar cc = Calendar.getInstance();


    private CycleRepository cycleRepository;


    @Override
    public void onCreate() {
        super.onCreate();

        cc.set(Calendar.DAY_OF_MONTH, 15);

        minDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, 2017);

        maxDate = Calendar.getInstance();
        maxDate.set(Calendar.YEAR, 2020);

        clueData = new ClueData(5, 25, 3, cc);
        cbClueData = new com.simorgh.cyclebar.ClueData(5, 25, 3, cc);
        cvClueData = new ClueView.ClueData(5, 25, 3);
        wdpClueData = new com.simorgh.weekdaypicker.ClueData(5, 25, 3, cc);

        cycleRepository = new CycleRepository(this);
        if (cycleRepository.getCycleData() == null) {
            Cycle initCycle = new Cycle();
            initCycle.setCycleID(1);
            initCycle.setRedDaysCount(3);
            initCycle.setGrayDaysCount(25);
            initCycle.setYellowDaysCount(3);
            initCycle.setStartDate(Calendar.getInstance());
            cycleRepository.insertCycle(initCycle);
        }
    }
}
