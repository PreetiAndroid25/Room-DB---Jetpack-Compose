package com.example.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room.model.Note
import com.example.room.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _noteList = MutableStateFlow(emptyList<Note>())
    val noteList = _noteList.asStateFlow()


    fun getNoteList() {
        viewModelScope.launch(IO) {
            repository.getAllNotes().collectLatest {
                _noteList.tryEmit(it)
            }
        }
    }

    fun updateNote(noteEntity: Note) {
        viewModelScope.launch(IO) {
            repository.update(noteEntity)
        }
    }

    fun insertNote(noteEntity: Note) {
        viewModelScope.launch(IO) {
            repository.insert(noteEntity)
        }
    }

    fun deleteNote(noteEntity: Note){
        viewModelScope.launch(IO) {
            repository.delete(noteEntity)
        }
    }
}
