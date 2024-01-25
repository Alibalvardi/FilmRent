package com.ali.filmrent.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "reserve")
data class Reserve(
    @PrimaryKey(autoGenerate = true)
    val reserve_id: Int? = null,
    val customer_id : Int,
    val store_id : Int,
    val film_id : Int,
    val reserveDate : Long
)
