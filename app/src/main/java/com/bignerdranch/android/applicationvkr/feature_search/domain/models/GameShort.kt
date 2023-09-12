package com.bignerdranch.android.applicationvkr.feature_search.domain.models

data class GameShort(
    val id: Int,
    val headerImage: String,
    val name: String,
    val publisher: String,
    val releaseDate: String,
    val tags: List<String>
)
