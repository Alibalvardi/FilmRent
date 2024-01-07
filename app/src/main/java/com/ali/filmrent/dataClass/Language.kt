package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"] , unique = true) ])
data class Language(
    @PrimaryKey(autoGenerate = true)
    val language_id :Int? = null ,
    val name : String
)
