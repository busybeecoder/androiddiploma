package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

import com.bignerdranch.android.applicationvkr.feature_profile.domain.models.LogoutResult
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository

class LogoutUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(): LogoutResult {
        return LogoutResult(
            result = repository.logout()
        )
    }
}
