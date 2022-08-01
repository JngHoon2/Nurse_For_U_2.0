package com.nfu2.LocalDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {LocalDB.class}, version = 1, exportSchema = false)
public abstract class LoginDB extends RoomDatabase{
    public abstract LocalDao localDao();
}

