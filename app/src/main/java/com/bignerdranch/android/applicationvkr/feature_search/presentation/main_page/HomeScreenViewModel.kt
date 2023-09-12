package com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.StandardTextFieldState
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.core.util.UiText
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.SearchUseCase
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.SetTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val setTagsUseCase: SetTagsUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _searchState = mutableStateOf(SearchState())
    val searchState: State<SearchState> = _searchState

//    private val _searchEmptyState = mutableStateOf(SearchState())
//    val searchEmptyState: State<SearchState> = _searchEmptyState

    private val _tags = mutableStateOf(TagState())
    val tags: State<TagState> = _tags

    val userInput by lazy { MutableLiveData("") }

    private val _queryState = mutableStateOf(StandardTextFieldState())
    val queryState: State<StandardTextFieldState> = _queryState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _onWastedToken = MutableSharedFlow<Unit>(replay = 1)
    val onWastedToken = _onWastedToken.asSharedFlow()

    fun onEvent(event: SearchGameEvent) {
        when (event) {
            is SearchGameEvent.EnteredQuery -> {
                _queryState.value = queryState.value.copy(
                    text = event.query
                )
            }
            is SearchGameEvent.ClearQuery -> {
                _queryState.value = queryState.value.copy(
                    text = ""
                )
            }
            is SearchGameEvent.OpenTagDialog -> {
                _searchState.value = searchState.value.copy(
                    isTagDialogVisible = true
                )
            }
            is SearchGameEvent.CloseTagDialog -> {
                _searchState.value = searchState.value.copy(
                    isTagDialogVisible = false
                )
            }
            is SearchGameEvent.SetTagsSelected -> {
                val result = setTagsUseCase.invoke(
                    selectedTags = tags.value.selectedTags,
                    event.tag
                )
                when (result) {
                    is Resource.Success -> {
                        _tags.value = result.data?.let {
                            tags.value.copy(
                                selectedTags = it
                            )
                        }!!
                    }
                }
            }
            is SearchGameEvent.DismissTagsSelected -> {
                _tags.value = tags.value.copy(
                    selectedTags = emptyList()
                )
            }
        }
    }

    fun searchGame() {
        viewModelScope.launch {
            _searchState.value = searchState.value.copy(
                isLoading = true
            )
            val searchGameResult = searchUseCase(
                query = queryState.value.text.lowercase(),
                tags = tags.value.selectedTags
            )
            when (searchGameResult) {
                is Resource.Success -> {
                    if (searchGameResult.data!!.isEmpty()) {
                        _searchState.value = searchState.value.copy(
                            isEmpty = true,
                            isLoading = false
                        )
                    } else {
                        _searchState.value = searchState.value.copy(
                            gameList = searchGameResult.data,
                            isLoading = false,
                            isEmpty = false,
                        )
                    }
                }
                is Resource.Error -> {
                    _searchState.value = searchState.value.copy(
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

    companion object {

        const val CHANGE_FROM_NAME_DESCENDING = 0L
        const val CHANGE_FROM_NAME_ASCENDING = 1L
        const val CHANGE_FROM_DATE_DESCENDING = 2L
        const val CHANGE_FROM_DATE_ASCENDING = 3L

        private const val NAME_BY_DESCENDING = 0L
        private const val NAME_BY_ASCENDING = 1L
        private const val DATE_BY_DESCENDING = 2L
        private const val DATE_BY_ASCENDING = 3L
        private const val NONE = 5L

        val date = SimpleDateFormat("dd.MM.yyyy")

        // compareBy для текста -> a < z (в обратном порядке)

        private val NameDescendingComparator =
            compareByDescending<GameRequest> { it.name.lowercase() }
        private val NameAscendingComparator =
            compareBy<GameRequest> { it.name.lowercase() }
        private val DateDescendingComparator =
            compareByDescending<GameRequest> {
                date.parse(it.releaseDate)
            }
        private val DateAscendingComparator =
            compareBy<GameRequest> { date.parse(it.releaseDate) }
    }

    var sortingState = NAME_BY_DESCENDING

    private fun sortGames() {
        when (sortingState) {
            NAME_BY_DESCENDING -> {
                _searchState.value.gameList = searchState.value.gameList.sortedWith(
                    NameDescendingComparator
                )
            }
            NAME_BY_ASCENDING -> {
                _searchState.value.gameList = searchState.value.gameList.sortedWith(
                    NameAscendingComparator
                )
            }
            DATE_BY_DESCENDING -> {
                _searchState.value.gameList = searchState.value.gameList.sortedWith(
                    DateDescendingComparator
                )
            }
            DATE_BY_ASCENDING -> {
                _searchState.value.gameList = searchState.value.gameList.sortedWith(
                    DateAscendingComparator
                )
            }
        }
    }

    fun changeState(changeFlag: Long) {
        sortingState = when (sortingState to changeFlag) {
            (NAME_BY_DESCENDING to CHANGE_FROM_NAME_ASCENDING) -> NAME_BY_ASCENDING
            (NAME_BY_DESCENDING to CHANGE_FROM_NAME_DESCENDING) -> NAME_BY_DESCENDING

            (NAME_BY_ASCENDING to CHANGE_FROM_NAME_ASCENDING) -> NAME_BY_ASCENDING
            (NAME_BY_ASCENDING to CHANGE_FROM_NAME_DESCENDING) -> NAME_BY_DESCENDING

            (NAME_BY_DESCENDING to CHANGE_FROM_DATE_DESCENDING),
            (NAME_BY_ASCENDING to CHANGE_FROM_DATE_DESCENDING) -> DATE_BY_DESCENDING

            (NAME_BY_DESCENDING to CHANGE_FROM_DATE_ASCENDING),
            (NAME_BY_ASCENDING to CHANGE_FROM_DATE_ASCENDING) -> DATE_BY_ASCENDING

            (DATE_BY_DESCENDING to CHANGE_FROM_NAME_ASCENDING),
            (DATE_BY_ASCENDING to CHANGE_FROM_NAME_ASCENDING) -> NAME_BY_ASCENDING

            (DATE_BY_DESCENDING to CHANGE_FROM_NAME_DESCENDING),
            (DATE_BY_ASCENDING to CHANGE_FROM_NAME_DESCENDING) -> NAME_BY_DESCENDING

            (DATE_BY_DESCENDING to CHANGE_FROM_DATE_ASCENDING) -> DATE_BY_ASCENDING
            (DATE_BY_DESCENDING to CHANGE_FROM_DATE_DESCENDING) -> DATE_BY_DESCENDING

            (DATE_BY_ASCENDING to CHANGE_FROM_DATE_ASCENDING) -> DATE_BY_ASCENDING
            (DATE_BY_ASCENDING to CHANGE_FROM_DATE_DESCENDING) -> DATE_BY_DESCENDING
            else -> NONE
        }
        sortGames()
    }
}
