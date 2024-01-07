package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Payment")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val payment_id: Int? = null,
    val customer_id : Int ,
    val store_id : Int ,
    val rental_id : Int ,
    val amount : Double ,
    //val payment_date
)
