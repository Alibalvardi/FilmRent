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
    @Query("SELECT SUM(amount) FROM payment WHERE customer_id=:customerId AND store_id=:storeId")
    fun sumOfPaymentOfEachCustomer(customerId: Int, storeId: Int): Int
    @Query("SELECT * FROM payment WHERE store_id=:storeId AND customer_id NOT LIKE 0 GROUP BY customer_id")
    fun listEachCustomerPaymentsOfStore(storeId: Int): List<Payment>




}