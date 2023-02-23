package com.notesapi.android.api

import com.notesapi.android.model.UserRequest
import com.notesapi.android.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest) : Response<UserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body userRequest: UserRequest) : Response<UserResponse>

    //@POST("/users/signup") enter end point of url
    //because of POST we have declare @Body
    //declare suspend fun for coroutines
    //this is retrofit interface
}