package com.example.uidesignlab;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import android.database.Cursor;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(UserRecord user);

    @Update
    int update(UserRecord user);

    @Delete
    int delete(UserRecord user);

    @Query("SELECT * FROM UserRecord ORDER BY id DESC")
    List<UserRecord> getAllUsers();

    @Query("DELETE FROM UserRecord")
    int deleteAll();

    // Methods for Content Provider

    @Query("SELECT * FROM UserRecord ORDER BY id DESC")
    Cursor getAllCursor();

    @Query("SELECT * FROM UserRecord WHERE id = :id")
    Cursor getByIdCursor(long id);

    @Query("UPDATE UserRecord SET name = :name WHERE id = :id")
    int updateName(long id, String name);

    @Query("DELETE FROM UserRecord WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT COUNT(*) FROM UserRecord WHERE name = :name")
    int countByName(String name);
}