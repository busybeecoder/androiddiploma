package com.bignerdranch.android.applicationvkr.feature_auth.presentation.util

import com.bignerdranch.android.applicationvkr.core.util.Error

sealed class AuthError : Error() {
    object FieldEmpty : AuthError()
    object InputTooShort : AuthError()
    object InvalidEmail : AuthError()
    object InvalidPassword : AuthError()
    object CreatedEmail : AuthError()
    object CreatedUsername : AuthError()
    object PasswordSimilarity : AuthError()
}
