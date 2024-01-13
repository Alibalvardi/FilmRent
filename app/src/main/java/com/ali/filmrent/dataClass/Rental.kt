package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental")
data class Rental(
    @PrimaryKey
    val rental_id : Int? = null ,
    val store_id : Int ,
    val customer_id : Int ,
    // val return date
    //val rental date
    val rentDuration : Int
)