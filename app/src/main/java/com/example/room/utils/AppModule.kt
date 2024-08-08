package com.example.room.utils

import android.app.Application
import androidx.room.Room
import com.example.room.database.NoteDatabase
import com.example.room.repository.NoteRepository
import com.example.room.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule
{
    @Provides
    @Singleton
    fun provideMyDataBase(app: Application): NoteDatabase
    {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "NoteDatabase"
        )
//            .addMigrations() later add migrations if u change the table
            .build()
    }


    @Provides
    @Singleton
    fun provideMyRepository(noteDatabase: NoteDatabase): Repository
    {
        return NoteRepository(noteDatabase.noteDao())
    }
}