package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

import com.bignerdranch.android.applicationvkr.core.domain.util.ValidationUtil
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError
import com.bignerdranch.android.applicationvkr.feature_profile.domain.models.ChangePasswordResult
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository

class ChangePasswordUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        current_password: String,
        new_password: String
    ): ChangePasswordResult {

        // TODO trim, validation изменить порядок

        val currentPasswordError = ValidationUtil.validatePassword(current_password)
        val passwordError = ValidationUtil.validatePassword(new_password)
        val similarityError =
            if (current_password == new_password) AuthError.PasswordSimilarity else null

        if (currentPasswordError != null || passwordError != null || similarityError != null) {
            return ChangePasswordResult(
                currentPasswordError = currentPasswordError,
                passwordError = passwordError,
                similarityError = similarityError
            )
        }

        val result = repository.changePassword(current_password.trim(), new_password.trim())

        return ChangePasswordResult(
            result = result
        )
    }
}
