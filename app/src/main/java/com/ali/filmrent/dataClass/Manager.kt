package com.ali.filmrent.dataClass

import androidx.room.*
import com.google.gson.Gson

@Entity(tableName = "manager")
data class Manager(
    @PrimaryKey(autoGenerate = true)
    val manager_id: Int? = null,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val username: String,
    val password: String,
    val email: String,
    var wallet: Int

)


