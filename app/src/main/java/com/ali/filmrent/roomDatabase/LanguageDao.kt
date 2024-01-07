package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ali.filmrent.dataClass.Language

@Dao
interface LanguageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLanguage(language: Language)
}