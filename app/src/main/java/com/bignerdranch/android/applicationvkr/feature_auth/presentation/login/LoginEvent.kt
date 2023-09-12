package com.bignerdranch.android.applicationvkr.feature_auth.presentation.login

sealed class LoginEvent {
    data class EnteredUsername(val username: String) : LoginEvent()
    data class EnteredPassword(val password: String) : LoginEvent()
    object Login : LoginEvent()
    object TogglePasswordVisibility : LoginEvent()
    object Navigate : LoginEvent()
}
