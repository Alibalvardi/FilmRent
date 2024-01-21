package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rating(
    @PrimaryKey(autoGenerate = true)
    val rating_id: Int? = null,
    val rating: Float,
    val store_id: Int,
    val customer_id: Int
)
