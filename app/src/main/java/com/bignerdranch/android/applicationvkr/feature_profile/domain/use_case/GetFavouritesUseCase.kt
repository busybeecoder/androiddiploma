package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest

class GetFavouritesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Resource<List<GameRequest>> {
        return repository.getFavourites()
    }
}
