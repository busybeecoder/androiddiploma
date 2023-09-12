package com.bignerdranch.android.applicationvkr.feature_auth.data.remote

data class AuthResponse(
    val access_token: String,
    val refresh_token: String,
    val error: String
)
