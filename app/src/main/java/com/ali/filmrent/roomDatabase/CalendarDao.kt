package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ali.filmrent.dataClass.AppCalendar

@Dao
interface CalendarDao {

    @Insert
    fun insert(calendar: AppCalendar)

    @Query("SELECT * FROM calender WHERE calender_id LIKE :calendarId")
    fun getCalendar(calendarId: Int): AppCalendar

    @Update
    fun updateCalendar(calendar: AppCalendar)
}