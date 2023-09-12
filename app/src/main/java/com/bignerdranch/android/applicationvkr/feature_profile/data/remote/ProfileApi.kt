package com.bignerdranch.android.applicationvkr.feature_profile.data.remote

import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.AuthResponse
import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.RegistrationResponse
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @POST("/private/change/email")
    suspend fun changeEmail(
        @Body request: ChangeEmailRequest
    ): Response<AuthResponse>

    @GET("/private/me")
    suspend fun getEmail(): Response<ProfileEmailRequest>

    @POST("/private/change/password")
    suspend fun changePassword(
        @Body request: ProfilePasswordRequest
    ): Response<AuthResponse>

    @POST("/logout")
    suspend fun logout(): Response<RegistrationResponse>

    @GET("/private/favourites")
    suspend fun getFavourites(): Response<List<GameRequest>>

    companion object {
        const val BASE_URL = "http://192.168.10.13:8000/"
    }
}
