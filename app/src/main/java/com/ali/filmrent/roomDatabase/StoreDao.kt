package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Store

@Dao
interface StoreDao {

    @Insert
    fun insertStore(store: Store)

    @Query("SELECT * FROM store WHERE manager_id LIKE :manager_id")
    fun listOfManagerStore(manager_id : Int) :List<Store>

    @Query("SELECT * FROM store")
    fun listOfAllStore() :List<Store>


    @Delete
    fun deleteStore(store: Store)
}