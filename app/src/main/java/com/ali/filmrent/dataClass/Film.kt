package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true)
    val film_id: Int? = null,
    val title: String,
    val yearOfRelease: Int,
    val language: String,
    val category: String,
    val actor: String,
    val rating: Float,
    val length: Int,
    val rentDuration: Int,
    val rentPerDay: Int,
    val amount: Int,
    val urlImg: String,
    val description : String
)

class FilmTypeConverters1 {

    @TypeConverter
    fun lanToString(language: Language): String = Gson().toJson(language)

    @TypeConverter
    fun stringToLanguage(string: String): Language = Gson().fromJson(string, Language::class.java)

}

class FilmTypeConverters2 {

    @TypeConverter
    fun catToString(category: Category): String = Gson().toJson(category)

    @TypeConverter
    fun stringToCategory(string: String): Category = Gson().fromJson(string, Category::class.java)

}

class FilmTypeConverters3 {
    @TypeConverter
    fun actorToString(actor: Actor): String = Gson().toJson(actor)

    @TypeConverter
    fun stringToActor(string: String): Actor = Gson().fromJson(string, Actor::class.java)
}