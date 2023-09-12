package com.bignerdranch.android.applicationvkr.feature_search.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FullGameRequest
import com.bignerdranch.android.applicationvkr.feature_search.domain.repository.HomeRepository

class GetGameUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Int): Resource<FullGameRequest> {
        return repository.getGame(id)
    }
}
