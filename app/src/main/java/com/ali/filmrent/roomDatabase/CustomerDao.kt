package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Customer

@Dao
interface CustomerDao {
    @Insert
    fun insertCustomer (customer: Customer)

    @Query("SELECT COALESCE((SELECT password FROM customer WHERE username = :username), 0) AS result")
    fun getPassword(username : String) : String
}