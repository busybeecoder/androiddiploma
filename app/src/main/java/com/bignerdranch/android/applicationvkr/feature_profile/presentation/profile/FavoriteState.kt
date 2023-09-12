package com.bignerdranch.android.applicationvkr.feature_profile.presentation.profile

import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest

data class FavoriteState(
    val isLoading: Boolean = false,
    var gameList: List<GameRequest> = emptyList(),
    val isEmpty: Boolean = false
)
