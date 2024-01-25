package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ali.filmrent.dataClass.Manager

@Dao
interface ManagerDao {

    @Insert
    fun insertManager(manager: Manager)

    @Query("SELECT COALESCE((SELECT password FROM manager WHERE username = :username), 0) AS result")
    fun getPassword(username: String): String

    @Query("SELECT  manager_id FROM manager WHERE username LIKE  :username")
    fun getId(username: String): Int

    @Query("SELECT * FROM manager WHERE manager_id LIKE :id ")
    fun getManagerById(id: Int): Manager

    @Query("UPDATE manager SET wallet = :newWallet WHERE manager_id LIKE :manager_id")
    fun updateWallet(manager_id: Int, newWallet: Int)
    @Update
    fun updateCustomer(newManager: Manager)
}