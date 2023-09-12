package com.bignerdranch.android.applicationvkr.feature_search.data.remote

import retrofit2.Response
import retrofit2.http.*

interface HomeApi {

    @POST("/private/games")
    suspend fun search(
        @Body query: SearchRequest
    ): Response<List<GameRequest>>

    @GET("/private/games/{id}")
    suspend fun getGame(
        @Path("id") id: Int
    ): Response<FullGameRequest>

    @POST("/private/favourites/add")
    suspend fun gameAddFavourite(
        @Body query: IdRequest
    ): Response<FavouriteRequest>

    @POST("/private/favourites/remove")
    suspend fun gameRemoveFavourite(
        @Body query: IdRequest
    ): Response<FavouriteRequest>

    companion object {
        const val BASE_URL = "http://192.168.10.13:8000/"
    }
}
