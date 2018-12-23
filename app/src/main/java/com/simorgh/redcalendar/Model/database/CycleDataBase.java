package com.simorgh.redcalendar.Model.database;

import android.content.Context;

import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.database.dao.CycleDAO;
import com.simorgh.redcalendar.Model.database.dao.DayMoodDAO;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.Model.database.model.DayMood;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cycle.class, DayMood.class}, version = 1)
@androidx.room.TypeConverters({TypeConverters.class})
public abstract class CycleDataBase extends RoomDatabase {

    abstract CycleDAO cycleDAO();

    abstract DayMoodDAO dayMoodDAO();

    private static volatile CycleDataBase INSTANCE;

    static CycleDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CycleDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CycleDataBase.class, AppManager.DB_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
