package com.simorgh.redcalendar.Model.database;

import android.app.Application;
import android.os.AsyncTask;

import com.simorgh.redcalendar.Model.database.dao.CycleDAO;
import com.simorgh.redcalendar.Model.database.dao.DayMoodDAO;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.Model.database.model.DayMood;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;

public class CycleRepository {
    private CycleDAO cycleDao;
    private DayMoodDAO dayMoodDAO;
    private LiveData<Cycle> cycleLiveData;

    public CycleRepository(Application application) {
        CycleDataBase db = CycleDataBase.getDatabase(application);
        cycleDao = db.cycleDAO();
        dayMoodDAO = db.dayMoodDAO();
        cycleLiveData = cycleDao.getLiveCycle(1);
    }

    public Cycle getCycleData() {
        return cycleDao.getCycle(1);
    }

    public LiveData<Cycle> getCycleLiveData() {
        return cycleLiveData;
    }

    public LiveData<List<DayMood>> getLiveDayMoodsData() {
        return dayMoodDAO.getLiveDayMoods();
    }

    public LiveData<DayMood> getLiveDayMoodData(Calendar calendar) {
        return dayMoodDAO.getLiveDayMood(calendar);
    }

    public void insertCycle(Cycle cycle) {
        new insertCycleAsyncTask(cycleDao).execute(cycle);
    }

    public void insertDayMood(DayMood dayMood) {
        new insertDayMoodAsyncTask(dayMoodDAO).execute(dayMood);
    }

    private static class insertCycleAsyncTask extends AsyncTask<Cycle, Void, Void> {

        private CycleDAO mAsyncTaskDao;

        insertCycleAsyncTask(CycleDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Cycle... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertDayMoodAsyncTask extends AsyncTask<DayMood, Void, Void> {

        private DayMoodDAO mAsyncTaskDao;

        insertDayMoodAsyncTask(DayMoodDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DayMood... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
