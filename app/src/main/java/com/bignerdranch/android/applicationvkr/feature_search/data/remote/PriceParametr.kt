package com.bignerdranch.android.applicationvkr.feature_search.data.remote

import com.google.gson.annotations.SerializedName

data class PriceParametr(
    @SerializedName("initial_formatted")
    val initialPrice: String,
    @SerializedName("final_formatted")
    val finalPrice: String,
    @SerializedName("discount_percent")
    val discount_percent: Int,
    @SerializedName("uri_string")
    val uri: String
)
