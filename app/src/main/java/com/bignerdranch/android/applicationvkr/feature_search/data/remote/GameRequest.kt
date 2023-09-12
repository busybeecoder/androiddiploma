package com.bignerdranch.android.applicationvkr.feature_search.data.remote

import com.bignerdranch.android.applicationvkr.feature_search.domain.models.GameShort
import com.google.gson.annotations.SerializedName

data class GameRequest(
    val id: Int,
    @SerializedName("header_image")
    val headerImage: String,
    val name: String,
    val publisher: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val tags: List<String>
) {
    fun toGameShort(): GameShort {
        return GameShort(
            id = id,
            headerImage = headerImage,
            name = name,
            publisher = publisher,
            releaseDate = releaseDate,
//            tags = tags.map { it.toTag() }
            tags = tags
        )
    }
}
