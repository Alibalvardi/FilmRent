package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val customer_id: Int? = null,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val email: String,
    val username: String,
    val password: String,
    val wallet : Int
)
