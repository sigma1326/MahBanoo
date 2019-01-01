package com.simorgh.databaseutils;

import android.content.Context;

import com.simorgh.databaseutils.dao.CycleDAO;
import com.simorgh.databaseutils.dao.DayMoodDAO;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cycle.class, DayMood.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters({TypeConverters.class})
public abstract class CycleDataBase extends RoomDatabase {
    public static final String DB_NAME = "red-calendar-db";


    abstract CycleDAO cycleDAO();

    abstract DayMoodDAO dayMoodDAO();

    private static volatile CycleDataBase INSTANCE;

    static CycleDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CycleDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CycleDataBase.class, DB_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}