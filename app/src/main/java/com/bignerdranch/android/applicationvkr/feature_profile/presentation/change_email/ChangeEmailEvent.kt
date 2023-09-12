package com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_email

sealed class ChangeEmailEvent {
    data class EnteredEmail(val email: String) : ChangeEmailEvent()
    object ChangeEmail : ChangeEmailEvent()
}
