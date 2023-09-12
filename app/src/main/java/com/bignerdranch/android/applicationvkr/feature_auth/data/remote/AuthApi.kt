package com.bignerdranch.android.applicationvkr.feature_auth.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/registration")
    suspend fun register(
        @Body request: CreateAccountRequest
    ): Response<RegistrationResponse>

    @POST("/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

//    @GET("/api/user/authenticate")
//    suspend fun authenticate()

    companion object {
        const val BASE_URL = "http://192.168.10.13:8000/"
    }
}
