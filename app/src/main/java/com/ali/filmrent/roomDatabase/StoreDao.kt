package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ali.filmrent.dataClass.Store

@Dao
interface StoreDao {

    @Insert
    fun insertStore(store: Store)

    @Query("SELECT * FROM store WHERE manager_id LIKE :manager_id")
    fun listOfManagerStore(manager_id: Int): List<Store>

    @Query("SELECT * FROM store")
    fun listOfAllStore(): List<Store>


    @Delete
    fun deleteStore(store: Store)

    @Query("SELECT * FROM store WHERE store_id LIKE :storeId")
    fun getStoreById(storeId: Int): Store

    @Query("SELECT manager_id FROM store WHERE store_id LIKE :storeId")
    fun getStoreManagerId(storeId: Int): Int
    @Update
    fun updateStore(newStore: Store)
    @Query("UPDATE store SET rating = :storeRate WHERE store_id = :storeId ")
    fun updateStoreRate(storeId: Int, storeRate: Float)




}