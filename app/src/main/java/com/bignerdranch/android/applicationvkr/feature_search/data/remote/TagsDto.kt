package com.bignerdranch.android.applicationvkr.feature_search.data.remote

import com.bignerdranch.android.applicationvkr.feature_search.domain.models.Tag

data class TagsDto(
    val tag: String,
) {
    fun toTag(): Tag {
        return Tag(
            tag = tag
        )
    }
}
