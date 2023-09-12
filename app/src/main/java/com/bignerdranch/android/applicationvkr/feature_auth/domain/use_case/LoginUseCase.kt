package com.bignerdranch.android.applicationvkr.feature_auth.domain.use_case

import com.bignerdranch.android.applicationvkr.core.domain.util.ValidationUtil
import com.bignerdranch.android.applicationvkr.feature_auth.domain.models.LoginResult
import com.bignerdranch.android.applicationvkr.feature_auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): LoginResult {
        val usernameError = ValidationUtil.validateUsername(username)
        val passwordError = ValidationUtil.validatePassword(password)

        if (usernameError != null || passwordError != null) {
            return LoginResult(
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        return LoginResult(
            result = repository.login(username, password)
        )
    }
}
