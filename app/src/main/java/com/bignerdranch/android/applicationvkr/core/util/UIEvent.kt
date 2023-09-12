package com.bignerdranch.android.applicationvkr.core.util

sealed class UIEvent : Event() {
    data class ShowSnackbar(val uiText: UiText) : UIEvent()
    data class Navigate(val route: String) : UIEvent()
    object NavigateUp : UIEvent()
    object OnLogin : UIEvent()
    object OnLogout : UIEvent()
}
