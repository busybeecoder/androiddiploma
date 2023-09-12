package com.bignerdranch.android.applicationvkr.feature_search.presentation.game_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.AddFavouriteGameUseCase
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.GetGameUseCase
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.RemoveFavouriteGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getGameUseCase: GetGameUseCase,
    private val addFavouriteGameUseCase: AddFavouriteGameUseCase,
    private val removeFavouriteGameUseCase: RemoveFavouriteGameUseCase
) : ViewModel() {

    private val _state = mutableStateOf(FullGameState())
    val state: State<FullGameState> = _state

    private val _favouriteState = mutableStateOf(FavouriteState())
    val favouriteState: State<FavouriteState> = _favouriteState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun addFavourite(id: Int) {
        viewModelScope.launch {
            when (addFavouriteGameUseCase.invoke(id)) {
                is Resource.Success -> {
                    _favouriteState.value = favouriteState.value.copy(
                        isFavourite = true
                    )
                    getGame(id)
                }
                is Resource.Error -> {
                    _favouriteState.value = favouriteState.value.copy(
                        isFavourite = false
                    )
                }
            }
        }
    }

    fun removeFavourite(id: Int) {
        viewModelScope.launch {
            when (removeFavouriteGameUseCase.invoke(id)) {
                is Resource.Success -> {
                    _favouriteState.value = favouriteState.value.copy(
                        isFavourite = false
                    )
                    getGame(id)
                }
                is Resource.Error -> {
                    _favouriteState.value = favouriteState.value.copy(
                        isFavourite = true
                    )
                }
            }
        }
    }

    fun getGame(id: Int) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )
            when (val result = getGameUseCase.invoke(id)) {
                is Resource.Success -> {
                    println("нет проблем")
                    _state.value =
                        result.data?.let {
                            state.value.copy(
                                fullGameRequest = it,
                                isLoading = false
                            )
                        }!!
                }
                is Resource.Error -> {
                    println("проблемы?")
                    _state.value = state.value.copy(fullGameRequest = null, isLoading = true)
//                    if (result.uiText == UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение.")) {
//                        _onWastedToken.emit(Unit)
//                    }
                    result.uiText?.let {
                        UIEvent.ShowSnackbar(it)
                    }?.let {
                        _eventFlow.emit(it)
                    }
                }
            }
        }
    }
}
