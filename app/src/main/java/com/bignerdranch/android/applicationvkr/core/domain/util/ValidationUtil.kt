package com.bignerdranch.android.applicationvkr.core.domain.util

import android.util.Patterns
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError
import java.util.regex.Pattern

object ValidationUtil {

    fun validateEmail(email: String): AuthError? {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            return AuthError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthError.InvalidEmail
        }
        return null
    }

    fun validateUsername(username: String): AuthError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()) {
            return AuthError.FieldEmpty
        }
        if (trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) {
            return AuthError.InputTooShort
        }
        return null
    }

    fun validatePassword(password: String): AuthError? {
        if (password.isBlank()) {
            return AuthError.FieldEmpty
        }
        if (password.length < Constants.MIN_PASSWORD_LENGTH) {
            return AuthError.InputTooShort
        }
        val capitalLettersInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        val special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
        val hasSpecial = special.matcher(password).find()
        if (!capitalLettersInPassword || !numberInPassword || !hasSpecial) {
            return AuthError.InvalidPassword
        }
        return null
    }
}
