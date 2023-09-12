package com.bignerdranch.android.applicationvkr.feature_profile.data.remote

data class ProfilePasswordRequest(
    val current_password: String,
    val new_password: String
)
