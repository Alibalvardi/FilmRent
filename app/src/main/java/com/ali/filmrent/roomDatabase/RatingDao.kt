package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Rating

@Dao
interface RatingDao {

    @Insert
    fun insertRating(rating: Rating)

    @Query("UPDATE rating SET rating = :rate WHERE store_id=:store_id AND customer_id=:customer_id")
    fun updateRating(rate: Float, store_id: Int, customer_id: Int)

    @Query("SELECT COUNT(*) FROM rating WHERE store_id =:store_id AND customer_id=:customer_id ")
    fun ifCustomerRatedStore(store_id: Int, customer_id: Int): Int

    @Query("SELECT rating FROM rating WHERE store_id =:store_id AND customer_id=:customer_id ")
    fun getRateIfRated(store_id: Int, customer_id: Int): Float

    @Query("SELECT AVG(rating) FROM Rating WHERE store_id = :store_id")
    fun avgStoreRating(store_id: Int): Float
}