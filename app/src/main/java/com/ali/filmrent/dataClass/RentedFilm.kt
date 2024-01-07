package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentedFilm")
data class RentedFilm(
    @PrimaryKey(autoGenerate = true)
    val rentedFilm_id : Int? = null ,
    val customer_id : Int ,
    val store_id : Int ,
    val during : Boolean
)

