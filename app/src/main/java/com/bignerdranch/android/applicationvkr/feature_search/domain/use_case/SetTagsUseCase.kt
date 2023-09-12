package com.bignerdranch.android.applicationvkr.feature_search.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_search.domain.models.Tag

class SetTagsUseCase {

    operator fun invoke(
        selectedTags: List<String>,
        tagToToggle: String
    ): Resource<List<String>> {
        val skillInList = selectedTags.find { it == tagToToggle }
        if (skillInList != null) {
            return Resource.Success(selectedTags - skillInList)
        }
        return Resource.Success(selectedTags + tagToToggle)
    }
}
