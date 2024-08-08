package com.example.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.room.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(studentEntity: Note)

    @Delete
    suspend fun delete(studentEntity: Note)

    @Update
    suspend fun update(studentEntity: Note)

    @Query("SELECT * FROM tbl_note")
    fun getAllNotes(): Flow<List<Note>>
}