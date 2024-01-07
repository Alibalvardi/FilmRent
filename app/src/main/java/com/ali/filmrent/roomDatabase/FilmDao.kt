package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import com.ali.filmrent.dataClass.Film

@Dao
interface FilmDao {
    @Insert
    fun insertListOfFilm(data :List<Film>)
}