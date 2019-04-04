package org.cs7cs3.team7.journeysharing.database.dao;

import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.Date;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void save(User user);

    @Query("SELECT * FROM user WHERE login = :login")
    User load(String login);

    @Query("SELECT * FROM user WHERE login = :login")
    User loadSync(String login);

    @Query("SELECT * FROM user WHERE login = :login AND lastRefresh > :lastRefreshMax LIMIT 1")
    User hasUser(String login, Date lastRefreshMax);


}