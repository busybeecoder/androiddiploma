package com.bignerdranch.android.applicationvkr.feature_profile.presentation.profile

sealed class ProfileEvent {
    data class EnteredEmail(val email: String) : ProfileEvent()
    data class EnteredCurrentPassword(val currentPassword: String) : ProfileEvent()
    data class EnteredNewPassword(val newPassword: String) : ProfileEvent()
    object ToggleCurrentPasswordVisibility : ProfileEvent()
    object ToggleNewPasswordVisibility : ProfileEvent()
    object ChangePassword : ProfileEvent()
    object Logout : ProfileEvent()
    object DismissChangeEmailDialog : ProfileEvent()
    object ShowChangeEmailDialog : ProfileEvent()
    object DismissChangePasswordDialog : ProfileEvent()
    object ShowChangePasswordDialog : ProfileEvent()
}
