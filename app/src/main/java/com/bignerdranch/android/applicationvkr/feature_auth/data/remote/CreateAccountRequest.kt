package com.bignerdranch.android.applicationvkr.feature_auth.data.remote

data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String
)
