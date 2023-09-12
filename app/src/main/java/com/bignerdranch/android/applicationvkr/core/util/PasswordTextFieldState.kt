package com.bignerdranch.android.applicationvkr.core.util

data class PasswordTextFieldState(
    val text: String = "",
    val error: Error? = null,

    val isPasswordVisible: Boolean = false
)
