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

    @Query("SELECT film_id FROM rental WHERE customer_id = :customerId AND returnDate = 0")
    fun getCustomerActiveFilms(customerId: Int): List<Int>

    @Query("SELECT * FROM rental WHERE customer_id = :customerId AND film_id =:filmId AND returnDate =0")
    fun listActiveRentsOfCustomer(customerId: Int, filmId: Int): List<Rental>

    @Query("SELECT * FROM rental WHERE store_id=:storeId AND returnDate =0")
    fun listActiveRentsOfStore(storeId : Int): List<Rental>
    @Query("UPDATE rental SET returnDate = :returnDate WHERE rental_id=:rentalId")
    fun insertReturnDate(rentalId: Int, returnDate: Long)
    @Query("SELECT COUNT(*) FROM rental WHERE film_id =:filmId AND returnDate =0")
    fun countOfActiveRentsOfFilmInAllStore(filmId: Int): Int
    @Query("SELECT COUNT(*) FROM rental WHERE store_id = :storeId AND film_id=:filmId AND returnDate =0")
    fun countOfActiveRentsOfFilmInStore(storeId: Int, filmId: Int): Int
    @Query("SELECT COUNT(*) FROM rental WHERE customer_id=:customerId")
    fun countOfRentsOfCustomer(customerId: Int): Int
    @Query("SELECT COUNT(*) FROM rental WHERE customer_id=:customerId AND returnDate=0")
    fun countOfActiveRentsOfCustomer(customerId: Int): Int


}