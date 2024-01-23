package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ali.filmrent.dataClass.AppCalendar
import java.util.Calendar

@Dao
interface CalendarDao {

    @Insert
    fun insert(calendar: AppCalendar)

    @Query("SELECT calendar FROM calender WHERE calender_id LIKE :calendarId")
    fun getCalendar(calendarId: Int): Long

    @Update
    fun updateCalendar(calendar: AppCalendar)
}