package com.simorgh.databaseutils.dao;

import com.simorgh.databaseutils.model.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("select * from users where id=1")
    User getUser();


    @Query("select * from users where id=1")
    LiveData<User> getUserLiveData();

    @Query("delete from users")
    void deleteAll();
}
