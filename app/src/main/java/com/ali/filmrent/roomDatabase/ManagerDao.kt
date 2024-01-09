package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Manager

@Dao
interface ManagerDao {

    @Insert
    fun insertManager (manager: Manager)

    @Query("SELECT COALESCE((SELECT password FROM manager WHERE username = :username), 0) AS result")
    fun getPassword(username : String) : String
}