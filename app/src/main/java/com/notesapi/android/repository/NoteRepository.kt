package com.notesapi.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.notesapi.android.api.NoteApi
import com.notesapi.android.model.NoteRequest
import com.notesapi.android.model.NoteResponse
import com.notesapi.android.model.UserResponse
import com.notesapi.android.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {

    private val _noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData : LiveData<NetworkResult<List<NoteResponse>>> = _noteLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>> = _statusLiveData

    suspend fun getNotes() {
        _noteLiveData.value = NetworkResult.Loading()
        val response = noteApi.getNotes()
        when {
            response.isSuccessful && response.body() != null -> {
                _noteLiveData.value = NetworkResult.Success(response.body())
            }
            response.errorBody() != null -> {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _noteLiveData.value = NetworkResult.Error(errorObj.getString("message"))
            }
            else -> {
                _noteLiveData.value = NetworkResult.Error("Something went wrong")
            }
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.value = NetworkResult.Loading()
        val response = noteApi.createNote(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.value = NetworkResult.Loading()
        val response = noteApi.updateNote(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.value = NetworkResult.Loading()
        val response = noteApi.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }

    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.value = NetworkResult.Success(message)
        } else {
            _statusLiveData.value = NetworkResult.Error("Something went wrong")
        }
    }
}