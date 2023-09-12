package com.bignerdranch.android.applicationvkr.feature_search.data.remote

import com.google.gson.annotations.SerializedName

data class FullGameRequest(
    val id: Int,
    @SerializedName("header_image")
    val headerImage: String,
    val name: String,
    val publisher: String,
    val description: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("is_favourite")
    val isFavourite: Boolean,
    val tags: List<String>,
    val prices: PricesDto
)
