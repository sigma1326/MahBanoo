package com.simorgh.databaseutils.dao;

import com.simorgh.databaseutils.model.Cycle;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CycleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cycle cycle);

    @Query("select * from cycles where start_date=:startDate")
    Cycle getCycle(Calendar startDate);

    @Query("select * from cycles order by start_date ASC")
    Cycle getCycles();

    @Query("select * from cycles where start_date=:startDate")
    LiveData<Cycle> getLiveCycle(Calendar startDate);

    @Query("select * from cycles order by start_date ASC")
    LiveData<List<Cycle>> getLiveCycles();

    @Query("delete from cycles")
    void deleteAll();
}
