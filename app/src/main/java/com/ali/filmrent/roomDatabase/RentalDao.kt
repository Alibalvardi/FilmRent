package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Rental

@Dao
interface RentalDao {

    @Query("SELECT COUNT(*) FROM rental WHERE store_id = :storeId AND film_id =:filmId AND returnDate =0")
    fun countOfActiveRentsOfFilm(storeId: Int, filmId: Int): Int
    @Query("SELECT COUNT(*) FROM rental WHERE store_id = :storeId AND returnDate =0")
    fun countOfActiveRentsOfStore(storeId: Int): Int
    @Insert
    fun insertRental(rental: Rental)


    @Query("SELECT COUNT(*) FROM rental WHERE customer_id = :customerId AND store_id=:storeId AND returnDate =0")
    fun countOfActiveRentsOfCustomerFromStore(storeId: Int, customerId: Int): Int

    @Query("SELECT film_id FROM rental WHERE customer_id LIKE :customerId")
    fun getCustomerFilms(customerId: Int): List<Int>




}