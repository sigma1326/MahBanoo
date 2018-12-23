package com.simorgh.redcalendar.Model.database.dao;

import com.simorgh.redcalendar.Model.database.model.Cycle;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CycleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cycle cycle);

    @Query("select * from cycle where cycle_id=:cycleId")
    Cycle getCycle(int cycleId);

    @Query("select * from cycle order by cycle_id ASC")
    Cycle getCycles();

    @Query("select * from cycle where cycle_id=:cycleId")
    LiveData<Cycle> getLiveCycle(int cycleId);

    @Query("delete from cycle")
    void deleteAll();
}
