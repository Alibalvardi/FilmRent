package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Film

@Dao
interface FilmDao {
    @Insert
    fun insertListOfFilm(data :List<Film>)

    @Query("SELECT * FROM film")
    fun getAllFilms(): List<Film>
    @Query("SELECT * FROM film WHERE film_id LIKE :filmId")
    fun getFilmById(filmId: Int): Film
    @Query("SELECT * FROM film WHERE film_id IN (:storeFilmsId)")
    fun getFilmsById(storeFilmsId: List<Int>): List<Film>




}