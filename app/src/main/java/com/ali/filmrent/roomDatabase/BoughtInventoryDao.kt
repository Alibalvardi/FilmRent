package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import com.ali.filmrent.dataClass.BoughtInventory

@Dao
interface BoughtInventoryDao {
    @Insert
    fun insert(boughtInventory: BoughtInventory)
}