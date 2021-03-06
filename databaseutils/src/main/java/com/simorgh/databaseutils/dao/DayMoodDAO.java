package com.simorgh.databaseutils.dao;

import com.simorgh.databaseutils.model.DayMood;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DayMoodDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DayMood dayMood);

    @Query("select * from day_mood where mood_id=:calendar")
    DayMood getDayMood(Calendar calendar);

    @Query("select * from day_mood where mood_id>=:start and mood_id<=:end order by mood_id ASC")
    List<DayMood> getMarkedDays(Calendar start, Calendar end);

    @Query("select * from day_mood order by mood_id ASC")
    DayMood getDayMoods();

    @Query("select * from day_mood where mood_id=:calendar")
    LiveData<DayMood> getLiveDayMood(Calendar calendar);

    @Query("select * from day_mood where mood_id>=:start and mood_id<=:end")
    List<DayMood> getDayMoodRange(Calendar start, Calendar end);

    @Query("select * from day_mood")
    LiveData<List<DayMood>> getLiveDayMoods();

    @Query("delete from day_mood")
    void deleteAll();
}
