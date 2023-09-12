package com.bignerdranch.android.applicationvkr.feature_search.presentation.game_page

import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FullGameRequest

data class FullGameState(
    val fullGameRequest: FullGameRequest? = null,
    val isLoading: Boolean = false,
)
