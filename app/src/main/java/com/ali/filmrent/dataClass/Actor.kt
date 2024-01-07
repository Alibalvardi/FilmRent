package com.ali.filmrent.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "actor", indices = [Index(value = ["name"] , unique = true) ])
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val actor_id: Int? = null,
    val name: String

)
