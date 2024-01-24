package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import com.ali.filmrent.dataClass.Payment

@Dao
interface PaymentDao {
    @Insert
    fun insertPayment(payment: Payment)


}