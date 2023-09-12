package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

import com.bignerdranch.android.applicationvkr.core.domain.util.ValidationUtil
import com.bignerdranch.android.applicationvkr.feature_profile.domain.models.ChangeEmailResult
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository

class ChangeEmailUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        email: String
    ): ChangeEmailResult {
        val emailError = ValidationUtil.validateEmail(email)

        if (emailError != null) {
            return ChangeEmailResult(
                emailError = emailError
            )
        }
        val result = repository.changeEmail(email.trim())

        return ChangeEmailResult(
            result = result
        )
    }
}
