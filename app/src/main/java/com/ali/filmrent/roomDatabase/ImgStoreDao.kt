package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.ImgStore

@Dao
interface ImgStoreDao {
    @Insert
    fun insertListOfUrl(data :List<ImgStore>)

    @Query("SELECT url FROM imgstore")
    fun listOfUrl() : List<String>
}