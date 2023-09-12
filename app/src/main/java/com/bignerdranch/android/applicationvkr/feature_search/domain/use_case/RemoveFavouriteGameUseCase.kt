package com.bignerdranch.android.applicationvkr.feature_search.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FavouriteRequest
import com.bignerdranch.android.applicationvkr.feature_search.domain.repository.HomeRepository

class RemoveFavouriteGameUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Int): Resource<FavouriteRequest> {
        return repository.removeGameFavourite(id)
    }
}
