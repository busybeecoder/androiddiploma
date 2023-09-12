package com.bignerdranch.android.applicationvkr.feature_auth.domain.models

import com.bignerdranch.android.applicationvkr.core.util.ErrorResource
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError

data class RegisterResult(
    val emailError: AuthError? = null,
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: ErrorResource? = null
)
