package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Reserve

@Dao
interface ReserveDao {

    @Insert
    fun insertReserve(reserve: Reserve)

    @Query("SELECT COUNT(*) FROM reserve WHERE store_id= :storeId AND customer_id=:customerID AND film_id=:filmId")
    fun countOfReserveOfCustomerFromStoreForFilm(customerID: Int, filmId: Int, storeId: Int): Int

    @Query("SELECT * FROM reserve WHERE store_id= :storeId")
    fun listOfReservationsOfStore(storeId: Int): List<Reserve>

    @Delete
    fun removeReserve(reserve: Reserve)
    @Query("SELECT * FROM reserve WHERE customer_id =:customerId")
    fun listOfReservationsOfCustomer(customerId: Int): List<Reserve>


}