package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "Payment")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val payment_id: Int? = null,
    val customer_id : Int ,
    val store_id : Int ,
    val rental_id : Int ,
    val amount : Int ,
    val settlementDate : Long
)
