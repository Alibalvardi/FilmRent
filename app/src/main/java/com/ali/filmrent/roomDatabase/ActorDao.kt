package com.ali.filmrent.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ali.filmrent.dataClass.Actor

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActor(actor: Actor)
}