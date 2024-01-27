package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.filmrent.dataClass.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategory(category: Category)
    @Query("SELECT name FROM category")
    fun getAllCategory(): List<String>
}