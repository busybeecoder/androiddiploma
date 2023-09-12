package com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page

data class TagState(
    val tags: List<String> = listOf(
//        Tag(
//            "strategy"
//        ),
//        Tag(
//            "shooter"
//        ),
//        Tag(
//            "horror"
//        )
        "action",
        "adventure",
        "indie",
        "early_access",
        "casual",
        "accounting",
        "strategy",
        "simulation",
        "free_to_play",
        "sports",
        "rpg",
        "racing",
        "massively_multiplayer",
        "animation_modeling",
        "design_illustration",
        "video_production",
        "utilities",
        "game_development"
    ),
    val selectedTags: List<String> = emptyList()
)
