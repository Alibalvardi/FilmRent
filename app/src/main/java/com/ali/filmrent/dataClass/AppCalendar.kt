package com.ali.filmrent.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Calendar

@Entity(tableName = "Calender")
data class AppCalendar(
    @PrimaryKey(autoGenerate = true)
    val calender_id : Int ?= null ,
    val calendar: Calendar
)
class TypeConvertersCalender {

    @TypeConverter
    fun calendarToString(calendar : Calendar): String = Gson().toJson(calendar)

    @TypeConverter
    fun stringToCalender(string: String): Calendar = Gson().fromJson(string, Calendar::class.java)

}
