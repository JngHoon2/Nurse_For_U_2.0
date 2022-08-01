package com.nfu2.LocalDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocalDao {

    @Query("SELECT * FROM LocalDB")
    List<LocalDB> getAll();

    @Insert
    void insert(LocalDB localDB);
}
