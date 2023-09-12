package com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.StandardTextFieldState
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.core.util.UiText
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.ChangeEmailUseCase
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.GetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangingEmailViewModel @Inject constructor(
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val getEmailUseCase: GetEmailUseCase
) : ViewModel() {
    private val _state = mutableStateOf(ChangeEmailState())
    val state: State<ChangeEmailState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _onChangedEmail = MutableSharedFlow<Unit>(replay = 1)
    val onChangedEmail = _onChangedEmail.asSharedFlow()

    private val _onWastedToken = MutableSharedFlow<Unit>(replay = 1)
    val onWastedToken = _onWastedToken.asSharedFlow()

    private val _emailState = mutableStateOf(StandardTextFieldState())
    val emailState: State<StandardTextFieldState> = _emailState

    fun onEvent(event: ChangeEmailEvent) {
        when (event) {
            is ChangeEmailEvent.EnteredEmail -> {
                _emailState.value = emailState.value.copy(
                    text = event.email
                )
            }
            is ChangeEmailEvent.ChangeEmail -> {
                changeEmail()
            }
        }
    }

    // TODO обработай ошибки в тостах

    fun getEmail() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )
            when (val result = getEmailUseCase.invoke()) {
                is Resource.Success -> {
                    println("нет проблем")
                    _emailState.value =
                        result.data?.email?.let { emailState.value.copy(text = it) }!!
                    _state.value = ChangeEmailState(isLoading = false)
                }
                is Resource.Error -> {
                    println("проблемы?")
                    _emailState.value = emailState.value.copy(text = "")
                    _state.value = ChangeEmailState(isLoading = false)
                    if (result.uiText == UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение.")) {
                        _onWastedToken.emit(Unit)
                    }
                    result.uiText?.let {
                        UIEvent.ShowSnackbar(it)
                    }?.let {
                        _eventFlow.emit(it)
                    }
                }
            }
        }
    }

    private fun changeEmail() {
        viewModelScope.launch {
            _emailState.value = emailState.value.copy(error = null)
            _state.value = ChangeEmailState(isLoading = true)
            val changeEmailResult = changeEmailUseCase(
                email = emailState.value.text
            )
            if (changeEmailResult.emailError != null) {
                _emailState.value = emailState.value.copy(
                    error = changeEmailResult.emailError
                )
            }
            when (changeEmailResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(UiText.StringResource(R.string.success_changed_email))
                    )
                    _onChangedEmail.emit(Unit)
                    _emailState.value = StandardTextFieldState(emailState.value.text)
                    _state.value = ChangeEmailState(isLoading = false)
                }
                is Resource.Error -> {
                    println("Здесь проблемы у change email")
                    changeEmailResult.result.uiText?.let {
                        UIEvent.ShowSnackbar(it)
                    }?.let {
                        _eventFlow.emit(it)
                    }
                    _state.value = ChangeEmailState(isLoading = false)
                    if (changeEmailResult.result.uiText == UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение.")) {
                        _onWastedToken.emit(Unit)
                    }
                }
                null -> {
                    changeEmailResult.result?.uiText?.let {
                        UIEvent.ShowSnackbar(it)
                    }?.let {
                        _eventFlow.emit(it)
                    }
                    _state.value = ChangeEmailState(isLoading = false)
                }
            }
        }
    }
}
