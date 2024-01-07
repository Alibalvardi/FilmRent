package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "store")
data class Store(
    @PrimaryKey
    val store_id :Int? =null ,
    val manager_id : Int ,
    val name : String ,
    val phoneNumber : String

)
