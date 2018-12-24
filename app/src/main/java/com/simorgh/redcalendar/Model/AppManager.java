package com.simorgh.redcalendar.Model;

import android.app.Application;

import com.simorgh.cycleutils.ClueData;
import com.simorgh.redcalendar.Model.database.CycleRepository;
import com.simorgh.redcalendar.Model.database.model.Cycle;

import java.util.Calendar;

public class AppManager extends Application {
    public static final String DB_NAME = "red-calendar-db";
    public static final String TAG = "debug13";
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

    }
}
