package com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_password

sealed class ChangePasswordEvent {
    data class EnteredCurrentPassword(val currentPassword: String) : ChangePasswordEvent()
    data class EnteredNewPassword(val newPassword: String) : ChangePasswordEvent()
    object ToggleCurrentPasswordVisibility : ChangePasswordEvent()
    object ToggleNewPasswordVisibility : ChangePasswordEvent()
    object ChangePassword : ChangePasswordEvent()
}
