package com.notesapi.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesapi.android.model.NoteRequest
import com.notesapi.android.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val noteLiveData get() = noteRepository.noteLiveData
    val statusLiveData get() = noteRepository.statusLiveData

    fun getNotes() = viewModelScope.launch {
        noteRepository.getNotes()
    }

    fun createNote(noteRequest: NoteRequest) = viewModelScope.launch {
        noteRepository.createNote(noteRequest)
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }
}