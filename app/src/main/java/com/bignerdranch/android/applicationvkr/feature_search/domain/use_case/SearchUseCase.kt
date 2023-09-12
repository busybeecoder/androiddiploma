package com.bignerdranch.android.applicationvkr.feature_search.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.bignerdranch.android.applicationvkr.feature_search.domain.models.Tag
import com.bignerdranch.android.applicationvkr.feature_search.domain.repository.HomeRepository

class SearchUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(query: String?, tags: List<String>?): Resource<List<GameRequest>> {
        return repository.search(query, tags)
    }
}
