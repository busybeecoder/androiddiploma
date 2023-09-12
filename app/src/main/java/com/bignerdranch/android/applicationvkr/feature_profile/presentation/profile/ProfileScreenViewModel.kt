package com.bignerdranch.android.applicationvkr.feature_profile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.applicationvkr.core.util.*
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.GetFavouritesUseCase
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private val _favouriteListState = mutableStateOf(FavoriteState())
    val favouriteListState: State<FavoriteState> = _favouriteListState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _onWastedToken = MutableSharedFlow<Unit>(replay = 1)
    val onWastedToken = _onWastedToken.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Logout -> {
                logout()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            val logoutResult = logoutUseCase()
            when (logoutResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UIEvent.OnLogout
                    )
                }
                is Resource.Error -> {
                    logoutResult.result.uiText?.let {
                        UIEvent.ShowSnackbar(
                            it
                        )
                    }?.let {
                        _eventFlow.emit(
                            it
                        )
                    }
                }
            }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            _favouriteListState.value = favouriteListState.value.copy(
                isLoading = true
            )
            when (val searchGameResult = getFavouritesUseCase.invoke()) {
                is Resource.Success -> {
                    println("нет проблем")
                    if (searchGameResult.data!!.isEmpty()) {
                        _favouriteListState.value = favouriteListState.value.copy(
                            isEmpty = true,
                            isLoading = false
                        )
                    } else {
                        _favouriteListState.value = favouriteListState.value.copy(
                            gameList = searchGameResult.data ?: emptyList(),
                            isLoading = false,
                            isEmpty = false
                        )
                    }
                }
                is Resource.Error -> {
                    _favouriteListState.value = favouriteListState.value.copy(
                        isLoading = false
                    )
                    if (searchGameResult.uiText == UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение.")) {
                        _onWastedToken.emit(Unit)
                    }
                    searchGameResult.uiText?.let {
                        UIEvent.ShowSnackbar(it)
                    }?.let {
                        _eventFlow.emit(it)
                    }
                }
            }
        }
    }
}
