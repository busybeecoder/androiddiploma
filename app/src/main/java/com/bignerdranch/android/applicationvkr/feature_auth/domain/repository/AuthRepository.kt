package com.bignerdranch.android.applicationvkr.feature_auth.domain.repository

import com.bignerdranch.android.applicationvkr.core.util.ErrorResource
import com.bignerdranch.android.applicationvkr.core.util.SimpleResource

interface AuthRepository {
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): ErrorResource

    suspend fun login(
        username: String,
        password: String
    ): SimpleResource
}
