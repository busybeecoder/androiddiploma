package com.bignerdranch.android.applicationvkr.feature_search.domain.repository

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.TokenResource
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FavouriteRequest
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FullGameRequest
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest

interface HomeRepository {

    suspend fun search(
        query: String?,
        tags: List<String>?
    ): Resource<List<GameRequest>>

    suspend fun getGame(id: Int): Resource<FullGameRequest>

    suspend fun addGameFavourite(id: Int): Resource<FavouriteRequest>

    suspend fun removeGameFavourite(id: Int): Resource<FavouriteRequest>

    suspend fun refreshingToken(): TokenResource
}
