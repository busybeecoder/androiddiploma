package com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page

sealed class SearchGameEvent {
    data class EnteredQuery(val query: String) : SearchGameEvent()
    object ClearQuery : SearchGameEvent()
    object Search : SearchGameEvent()
    object OpenTagDialog : SearchGameEvent()
    object DismissTagsSelected : SearchGameEvent()
    object CloseTagDialog : SearchGameEvent()
    object Navigate : SearchGameEvent()
    data class SetTagsSelected(val tag: String) : SearchGameEvent()
}
