package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImgStore(
    @PrimaryKey
    val url : String
)
