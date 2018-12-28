package com.simorgh.databaseutils;

import android.app.Application;
import android.os.AsyncTask;

import com.simorgh.databaseutils.dao.CycleDAO;
import com.simorgh.databaseutils.dao.DayMoodDAO;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;

public class CycleRepository {
    private CycleDAO cycleDao;
    private DayMoodDAO dayMoodDAO;
    private LiveData<Cycle> cycleLiveData;
    private LiveData<List<DayMood>> listLiveData;

    public CycleRepository(Application application) {
        CycleDataBase db = CycleDataBase.getDatabase(application);
        cycleDao = db.cycleDAO();
        dayMoodDAO = db.dayMoodDAO();
        cycleLiveData = cycleDao.getLiveCycle(1);
        listLiveData = dayMoodDAO.getLiveDayMoods();
    }

    public Cycle getCycleData() {
        return cycleDao.getCycle(1);
    }

    public DayMood getDayMood(Calendar calendar) {
        return dayMoodDAO.getDayMood(calendar);
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

    public LiveData<List<DayMood>> getListLiveData() {
        return listLiveData;
    }

    public void setListLiveData(LiveData<List<DayMood>> listLiveData) {
        this.listLiveData = listLiveData;
    }

    public void clearData() {
        new ClearDataAsyncTask(cycleDao, dayMoodDAO).execute();
    }

    public void insertCycle(Cycle cycle) {
        new insertCycleAsyncTask(cycleDao).execute(cycle);
    }

    public void insertDayMood(DayMood dayMood) {
        new insertDayMoodAsyncTask(dayMoodDAO).execute(dayMood);
    }

    private static class ClearDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private CycleDAO cycleDAO;
        private DayMoodDAO dayMoodDAO;

        ClearDataAsyncTask(CycleDAO cycleDAO, DayMoodDAO dayMoodDAO) {
            this.cycleDAO = cycleDAO;
            this.dayMoodDAO = dayMoodDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cycleDAO.deleteAll();
            dayMoodDAO.deleteAll();
            return null;
        }
    }

    private static class insertCycleAsyncTask extends android.os.AsyncTask<Cycle, Void, Void> {

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
