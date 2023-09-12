package com.bignerdranch.android.applicationvkr.feature_profile.domain.models

import com.bignerdranch.android.applicationvkr.core.util.SimpleResource
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError

data class ChangePasswordResult(
    val currentPasswordError: AuthError? = null,
    val passwordError: AuthError? = null,
    val similarityError: AuthError? = null,
    val result: SimpleResource? = null
)
