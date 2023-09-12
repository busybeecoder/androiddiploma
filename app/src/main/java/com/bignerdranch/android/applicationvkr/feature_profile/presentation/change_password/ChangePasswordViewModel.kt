package com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.util.*
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ChangePasswordState())
    val state: State<ChangePasswordState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _onChangedPassword = MutableSharedFlow<Unit>(replay = 1)
    val onChangedPassword = _onChangedPassword.asSharedFlow()

    private val _onWastedToken = MutableSharedFlow<Unit>(replay = 1)
    val onWastedToken = _onWastedToken.asSharedFlow()

    private val _currentPasswordState = mutableStateOf(PasswordTextFieldState())
    val currentPasswordState: State<PasswordTextFieldState> = _currentPasswordState

    private val _newPasswordState = mutableStateOf(PasswordTextFieldState())
    val newPasswordState: State<PasswordTextFieldState> = _newPasswordState

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.EnteredCurrentPassword -> {
                _currentPasswordState.value = currentPasswordState.value.copy(
                    text = event.currentPassword
                )
            }
            is ChangePasswordEvent.EnteredNewPassword -> {
                _newPasswordState.value = newPasswordState.value.copy(
                    text = event.newPassword
                )
            }
            is ChangePasswordEvent.ToggleCurrentPasswordVisibility -> {
                _currentPasswordState.value = _currentPasswordState.value.copy(
                    isPasswordVisible = !currentPasswordState.value.isPasswordVisible
                )
            }
            is ChangePasswordEvent.ToggleNewPasswordVisibility -> {
                _newPasswordState.value = _newPasswordState.value.copy(
                    isPasswordVisible = !newPasswordState.value.isPasswordVisible
                )
            }
            is ChangePasswordEvent.ChangePassword -> {
                viewModelScope.launch {
                    _currentPasswordState.value = currentPasswordState.value.copy(error = null)
                    _newPasswordState.value = newPasswordState.value.copy(error = null)
                    _state.value = ChangePasswordState(isLoading = true)
                    val changePasswordResult = changePasswordUseCase(
                        current_password = currentPasswordState.value.text,
                        new_password = newPasswordState.value.text
                    )
                    if (changePasswordResult.currentPasswordError != null) {
                        _currentPasswordState.value = currentPasswordState.value.copy(
                            error = changePasswordResult.currentPasswordError
                        )
                    }
                    if (changePasswordResult.passwordError != null) {
                        _newPasswordState.value = newPasswordState.value.copy(
                            error = changePasswordResult.passwordError
                        )
                    }
                    if (changePasswordResult.similarityError != null) {
                        _currentPasswordState.value = currentPasswordState.value.copy(
                            error = changePasswordResult.similarityError
                        )
                        _newPasswordState.value = newPasswordState.value.copy(
                            error = changePasswordResult.similarityError
                        )
                    }
                    when (changePasswordResult.result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(UiText.StringResource(R.string.success_new_password))
                            )
                            _onChangedPassword.emit(Unit)
                            _state.value = ChangePasswordState(isLoading = false)
                        }
                        is Resource.Error -> {
                            println("Здесь проблемы у change password")
                            if (changePasswordResult.result.uiText == UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение.")) {
                                _onWastedToken.emit(Unit)
                            }
                            changePasswordResult.result.uiText?.let { UIEvent.ShowSnackbar(it) }
                                ?.let {
                                    _eventFlow.emit(
                                        it
                                    )
                                }
                            _state.value = ChangePasswordState(isLoading = false)
                        }
                        null -> {
                            _state.value = ChangePasswordState(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}
