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

    @Query("SELECT * FROM film WHERE category LIKE'%'|| :category || '%'")
    fun getAllFilms(category: String): List<Film>
    @Query("SELECT * FROM film WHERE film_id LIKE :filmId")
    fun getFilmById(filmId: Int): Film
    @Query("SELECT * FROM film WHERE film_id IN (:storeFilmsId)")
    fun getFilmsById(storeFilmsId: List<Int>): List<Film>
    @Query("SELECT * FROM film WHERE title LIKE '%'|| :searching || '%' AND category LIKE'%'|| :category || '%'")
    fun searchTitleFilms(searching: String , category: String): List<Film>
    @Query("SELECT * FROM film WHERE category LIKE :category")
    fun getCategoryFilm(category: String): List<Film>

    @Query("SELECT * FROM film WHERE actor LIKE '%'|| :searching || '%' AND category LIKE'%'|| :category || '%'")
    fun searchActorFilms(searching: String , category: String): List<Film>

    @Query("SELECT * FROM film WHERE language LIKE '%'|| :searching || '%' AND category LIKE'%'|| :category || '%'")
    fun searchLanguageFilms(searching: String , category: String): List<Film>


    @Query("SELECT * FROM film WHERE yearOfRelease LIKE '%'|| :searching || '%' AND category LIKE'%'|| :category || '%'")
    fun searchYearFilms(searching: String , category: String): List<Film>
}