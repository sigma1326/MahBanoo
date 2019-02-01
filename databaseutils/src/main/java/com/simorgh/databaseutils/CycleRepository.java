package com.simorgh.databaseutils;

import android.app.Application;
import android.os.AsyncTask;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.databaseutils.dao.CycleDAO;
import com.simorgh.databaseutils.dao.DayMoodDAO;
import com.simorgh.databaseutils.dao.UserDAO;
import com.simorgh.databaseutils.dao.UserWithCyclesDAO;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.databaseutils.model.User;
import com.simorgh.databaseutils.model.UserWithCycles;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CycleRepository {
    private CycleDAO cycleDao;
    private DayMoodDAO dayMoodDAO;
    private UserDAO userDAO;
    private UserWithCyclesDAO userWithCyclesDAO;
    //    private LiveData<Cycle> cycleLiveData;
    private LiveData<List<DayMood>> moodsLiveData;
    private LiveData<UserWithCycles> userWithCyclesLiveData;

    public CycleRepository(Application application) {
        CycleDataBase db = CycleDataBase.getDatabase(application);
        cycleDao = db.cycleDAO();
        dayMoodDAO = db.dayMoodDAO();
        userDAO = db.userDAO();
        userWithCyclesDAO = db.userWithCyclesDAO();
        moodsLiveData = dayMoodDAO.getLiveDayMoods();
        userWithCyclesLiveData = userWithCyclesDAO.getUserWithCyclesLiveData();
    }

    public Cycle getCycleData() {
        if (userWithCyclesDAO.getUserWithCycles() == null) {
            return null;
        }
        if (userWithCyclesDAO.getUserWithCycles().getCycles() == null) {
            return null;
        }
        if (userWithCyclesDAO.getUserWithCycles().getCycles().size() == 0) {
            return null;
        }
        return userWithCyclesDAO.getUserWithCycles().getCycles().get(0);
//        for (Cycle cycle : userWithCyclesDAO.getUserWithCycles().getCycles()) {
//            if (cycle.getStartDate().getTimeInMillis()
//                    == userWithCyclesDAO.getUserWithCycles().getUser().getCurrentCycle().getTimeInMillis()) {
//                return cycle;
//            }
//        }
//        return null;
    }

    private Calendar start = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();

    public List<DayMood> getMonthMarkedDays(Calendar calendar) {
        start.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        int days = CalendarTool.getDaysInMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), CalendarType.GREGORIAN);
        end.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), days, 0, 0, 0);
        return dayMoodDAO.getMarkedDays(start, end);
    }

    public DayMood getDayMood(Calendar calendar) {
        return dayMoodDAO.getDayMood(calendar);
    }

    public UserWithCycles getUserWithCycles() {
        return userWithCyclesDAO.getUserWithCycles();
    }

    public LiveData<UserWithCycles> getUserWithCyclesLiveData() {
        return userWithCyclesLiveData;
    }

    public LiveData<List<DayMood>> getLiveDayMoodsData() {
        return dayMoodDAO.getLiveDayMoods();
    }

    public LiveData<DayMood> getLiveDayMoodData(Calendar calendar) {
        return dayMoodDAO.getLiveDayMood(calendar);
    }

    public LiveData<List<DayMood>> getMoodsLiveData() {
        return moodsLiveData;
    }

    public void setMoodsLiveData(LiveData<List<DayMood>> moodsLiveData) {
        this.moodsLiveData = moodsLiveData;
    }

    public List<DayMood> getDayMoodRange(@NonNull Calendar start, @NonNull Calendar end) {
        return dayMoodDAO.getDayMoodRange(start, end);
    }

    public void clearData() {
        new ClearDataAsyncTask(cycleDao, dayMoodDAO).execute();
    }

    public void insertCycle(Cycle cycle) {
        new insertCycleAsyncTask(cycleDao).execute(cycle);
    }

    public void insertUser(User user) {
        new insertUserAsyncTask(userDAO).execute(user);
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

    private static class insertUserAsyncTask extends android.os.AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        insertUserAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
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
