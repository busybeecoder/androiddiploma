package com.bignerdranch.android.applicationvkr.core.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {
    @POST("/token/refresh")
    suspend fun refreshAccessToken(
        @Body request: TokenRequest
    ): Response<TokenResponse>

    companion object {
        const val BASE_URL = "http://192.168.10.13:8000/"
    }
}
