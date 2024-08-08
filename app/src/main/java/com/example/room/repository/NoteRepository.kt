package com.example.room.repository

import com.example.room.dao.NoteDao
import com.example.room.model.Note
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Repository {

    suspend fun insert(noteEntity: Note)

    suspend fun delete(noteEntity: Note)

    suspend fun update(noteEntity: Note)

    suspend fun getAllNotes(): Flow<List<Note>>
}

class NoteRepository @Inject constructor(
    private val dao: NoteDao,
) : Repository {
    override suspend fun insert(noteEntity: Note) {
        withContext(IO) {
            dao.insert(noteEntity)
        }
    }

    override suspend fun delete(noteEntity: Note) {
        withContext(IO) {
            dao.delete(noteEntity)
        }
    }

    override suspend fun update(noteEntity: Note) {
        withContext(IO) {
            dao.update(noteEntity)
        }
    }

    override suspend fun getAllNotes(): Flow<List<Note>> {
        return withContext(IO) {
            dao.getAllNotes()
        }
    }

}