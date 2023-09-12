package com.bignerdranch.android.applicationvkr.feature_profile.domain.models

import com.bignerdranch.android.applicationvkr.core.util.SimpleResource
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError

data class ChangeEmailResult(
    val emailError: AuthError? = null,
    val result: SimpleResource? = null
)
