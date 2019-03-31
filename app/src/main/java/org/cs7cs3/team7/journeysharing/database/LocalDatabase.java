package org.cs7cs3.team7.journeysharing.database;

import org.cs7cs3.team7.journeysharing.database.converter.DateConverter;
import org.cs7cs3.team7.journeysharing.database.dao.UserDao;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class LocalDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile LocalDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();
}
