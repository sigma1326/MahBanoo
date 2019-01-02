package com.simorgh.databaseutils.dao;

import com.simorgh.databaseutils.model.UserWithCycles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserWithCyclesDAO {
    @Query("select * from users")
    LiveData<UserWithCycles> getUserWithCyclesLiveData();

    @Query("select * from users ")
    UserWithCycles getUserWithCycles();


    @Query("delete from users")
    void deleteAll();
}
