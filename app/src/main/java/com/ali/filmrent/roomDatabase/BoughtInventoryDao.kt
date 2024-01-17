package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.BoughtInventory

@Dao
interface BoughtInventoryDao {
    @Insert
    fun insert(boughtInventory: BoughtInventory)

    @Query("SELECT DISTINCT film_id FROM buyedInventory WHERE store_id LIKE :storeId")
    fun getStoreFilms(storeId: Int): List<Int>
    @Query("SELECT film_id FROM buyedInventory WHERE store_id LIKE :storeId")
    fun getStoreAllCopies(storeId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM buyedInventory WHERE film_id LIKE :filmID AND store_id LIKE :storeId")
    fun countOfFilm(storeId: Int, filmID: Int): Int


}