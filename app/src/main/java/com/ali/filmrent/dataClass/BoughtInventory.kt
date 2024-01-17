package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buyedInventory")
data class BoughtInventory(
    @PrimaryKey(autoGenerate = true)
    val bayedInventory_id: Int? = null,
    val film_id: Int,
    val store_id: Int
)
