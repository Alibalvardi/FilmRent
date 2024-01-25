package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.dataClass.Store

@Dao
interface ReserveDao {

    @Insert
    fun insertReserve(reserve: Reserve)

    @Query("SELECT COUNT(*) FROM reserve WHERE store_id= :storeId AND customer_id=:customerID AND film_id=:filmId")
    fun countOfReserveOfCustomerFromStoreForFilm(customerID: Int, filmId: Int, storeId: Int): Int
}