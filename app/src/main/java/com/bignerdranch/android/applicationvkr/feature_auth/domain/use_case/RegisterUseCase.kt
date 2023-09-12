package com.bignerdranch.android.applicationvkr.feature_auth.domain.use_case

import com.bignerdranch.android.applicationvkr.core.domain.util.ValidationUtil
import com.bignerdranch.android.applicationvkr.feature_auth.domain.models.RegisterResult
import com.bignerdranch.android.applicationvkr.feature_auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String
    ): RegisterResult {
        val emailError = ValidationUtil.validateEmail(email)
        val usernameError = ValidationUtil.validateUsername(username)
        val passwordError = ValidationUtil.validatePassword(password)

        if (emailError != null || usernameError != null || passwordError != null) {
            return RegisterResult(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }

        val result = repository.register(email.trim(), username.trim(), password.trim())

        return RegisterResult(
            result = result
        )
    }
}
