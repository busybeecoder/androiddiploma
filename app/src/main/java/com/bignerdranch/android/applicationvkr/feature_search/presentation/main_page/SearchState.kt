package com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page

import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.bignerdranch.android.applicationvkr.feature_search.domain.models.GameShort

data class SearchState(
    val isLoading: Boolean = false,
    var gameList: List<GameRequest> = emptyList(),
    val isEmpty: Boolean = false,
//    = listOf(
//        GameShort(
//            id = 1,
//            headerImage = "https://images-ext-2.discordapp.net/external/NIhOs7QMk2TxBUmvmil7NfubimJqmvVJeGD53z-3zvA/%3Ft%3D1646868319/https/cdn.akamai.steamstatic.com//steam//apps//266410//header.jpg",
//            name = "iRacing",
//            publisher = "Steam",
//            releaseDate = "26.08.2008",
//            tags = listOf(
//                Tag("Racing"), Tag("Simulator"), Tag("Adventure"), Tag("Adventure")
//            )
//        ),
//        GameShort(
//            id = 2,
//            headerImage = "https://cdn.akamai.steamstatic.com/steam/apps/413150/header.jpg?t=1608624324",
//            name = "Stardew Valley",
//            publisher = "Steam",
//            releaseDate = "26.02.2016",
//            tags = listOf(
//                Tag("Simulator")
//            )
//        ),
//        GameShort(
//            id = 3,
//            headerImage = "https://cdn.akamai.steamstatic.com/steam/apps/570/header.jpg?t=1645731580",
//            name = "Dota 2",
//            publisher = "Steam",
//            releaseDate = "09.07.2013",
//            tags = listOf(
//                Tag("Simulator")
//            )
//        )
//
//    ),
    var isTagDialogVisible: Boolean = false
)
