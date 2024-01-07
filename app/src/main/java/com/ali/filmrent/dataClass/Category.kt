package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "category", indices = [Index(value = ["name"] , unique = true) ])
data class Category(
    @PrimaryKey(autoGenerate = true)
    val category_id: Int? =null ,
    val name :String
)
