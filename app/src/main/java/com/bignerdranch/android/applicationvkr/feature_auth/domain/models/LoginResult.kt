package com.bignerdranch.android.applicationvkr.feature_auth.domain.models

import com.bignerdranch.android.applicationvkr.core.util.SimpleResource
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError

data class LoginResult(
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)
