package com.notesapi.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.notesapi.android.api.UserApi
import com.notesapi.android.model.UserRequest
import com.notesapi.android.model.UserResponse
import com.notesapi.android.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>> = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.value = NetworkResult.Loading()
        val response = userApi.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.value = NetworkResult.Loading()
        val response = userApi.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        when {
            response.isSuccessful && response.body() != null -> {
                _userResponseLiveData.value = NetworkResult.Success(response.body())
            }
            response.errorBody() != null -> {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userResponseLiveData.value = NetworkResult.Error(errorObj.getString("message")) //error response key
            }
            else -> {
                _userResponseLiveData.value = NetworkResult.Error("Something went wrong")
            }
        }
    }
}