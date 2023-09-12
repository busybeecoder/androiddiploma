package com.bignerdranch.android.applicationvkr.feature_profile.domain.repository

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.SimpleResource
import com.bignerdranch.android.applicationvkr.core.util.TokenResource
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfileEmailRequest
import com.bignerdranch.android.applicationvkr.feature_profile.domain.models.Profile
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest

interface ProfileRepository {

    suspend fun getProfile(userId: String): Resource<Profile>

    suspend fun logout(): SimpleResource

    suspend fun getEmail(): Resource<ProfileEmailRequest>

    suspend fun changeEmail(
        email: String
    ): SimpleResource

    suspend fun changePassword(
        current_password: String,
        new_password: String
    ): SimpleResource

    suspend fun getFavourites(): Resource<List<GameRequest>>

    suspend fun refreshingToken(): TokenResource
}
