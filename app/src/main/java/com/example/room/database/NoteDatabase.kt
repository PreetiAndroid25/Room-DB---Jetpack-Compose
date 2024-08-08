package com.example.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.room.dao.NoteDao
import com.example.room.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
