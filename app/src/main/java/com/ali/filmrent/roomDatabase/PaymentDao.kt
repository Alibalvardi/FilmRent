package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Payment

@Dao
interface PaymentDao {
    @Insert
    fun insertPayment(payment: Payment)
    @Query("SELECT * FROM payment WHERE store_id=:storeId")
    fun listPaymentsOfStore(storeId: Int): List<Payment>
    @Query("SELECT * FROM payment WHERE customer_id=:customerId")
    fun listPaymentsOfCustomer(customerId: Int): List<Payment>


}