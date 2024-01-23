package com.ali.filmrent.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Calendar

@Entity(tableName = "rental")
data class Rental(
    @PrimaryKey
    val rental_id: Int? = null,
    val store_id: Int,
    val customer_id: Int,
    val film_id: Int,
    val rentalDate: Long,
    var returnDate: Long,
    val rentDuration: Int
)

