package com.simorgh.databaseutils.dao;

import com.simorgh.databaseutils.model.UserWithCycles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface UserWithCyclesDAO {
    @Transaction
    @Query("select * from users")
    LiveData<UserWithCycles> getUserWithCyclesLiveData();

    @Transaction
    @Query("select * from users ")
    UserWithCycles getUserWithCycles();


    @Transaction
    @Query("delete from users")
    void deleteAll();
}
