package com.bignerdranch.android.applicationvkr.feature_search.data.repository

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRefreshApi
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRequest
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.TokenResource
import com.bignerdranch.android.applicationvkr.core.util.UiText
import com.bignerdranch.android.applicationvkr.feature_auth.data.repository.ErrorResponse
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.*
import com.bignerdranch.android.applicationvkr.feature_search.domain.repository.HomeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

class HomeRepositoryImpl(
    private val homeApi: HomeApi,
    private val sharedPreferences: SharedPreferences,
    private val tokenRefreshApi: TokenRefreshApi
) : HomeRepository {

    override suspend fun search(query: String?, tags: List<String>?): Resource<List<GameRequest>> {
        val request = SearchRequest(query, tags)
        try {
            val response = homeApi.search(request)
            println(response.body())
            return if (response.isSuccessful) {
                Resource.Success(
                    data = response.body()
                )
            } else {
                coroutineScope {
                    val refreshToken = async { refreshingToken() }
                    val a = refreshToken.await()
                    println(a)
                    when (refreshToken.await()) {
                        is Resource.Success -> {
                            println("Здесь мы его поменяли")
                            search(query, tags)
                        }
                        is Resource.Error -> {
                            println("Сюда ненада getEmail")
                            sharedPreferences.edit()
                                .putString(Constants.KEY_JWT_TOKEN, null)
                                .putString(Constants.KEY_REFRESH_TOKEN, null)
                                .putString(Constants.KEY_USER_ID, "")
                                .apply()
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_REFRESH_TOKEN,
                                    null
                                )
                                } refresh token"
                            )
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_JWT_TOKEN,
                                    null
                                )
                                } access token"
                            )
                            Resource.Error(
                                uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        }
    }

    override suspend fun getGame(id: Int): Resource<FullGameRequest> {
        try {
            val response = homeApi.getGame(id)
            println(response)
            return if (response.isSuccessful) {
                Resource.Success(
                    data = response.body()
                )
            } else {
                coroutineScope {
                    val refreshToken = async { refreshingToken() }
                    val a = refreshToken.await()
                    println(a)
                    when (refreshToken.await()) {
                        is Resource.Success -> {
                            println("Здесь мы его поменяли")
                            getGame(id)
                        }
                        is Resource.Error -> {
                            println("Сюда ненада getEmail")
                            sharedPreferences.edit()
                                .putString(Constants.KEY_JWT_TOKEN, null)
                                .putString(Constants.KEY_REFRESH_TOKEN, null)
                                .putString(Constants.KEY_USER_ID, "")
                                .apply()
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_REFRESH_TOKEN,
                                    null
                                )
                                } refresh token"
                            )
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_JWT_TOKEN,
                                    null
                                )
                                } access token"
                            )
                            Resource.Error(
                                uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        }
    }

    override suspend fun addGameFavourite(id: Int): Resource<FavouriteRequest> {
        val request = IdRequest(id)
        try {
            val response = homeApi.gameAddFavourite(request)
            return if (response.isSuccessful) {
                Resource.Success(null)
            } else {
                coroutineScope {
                    val refreshToken = async { refreshingToken() }
                    val a = refreshToken.await()
                    println(a)
                    when (refreshToken.await()) {
                        is Resource.Success -> {
                            println("Здесь мы его поменяли")
                            addGameFavourite(id)
                        }
                        is Resource.Error -> {
                            println("Сюда ненада getEmail")
                            sharedPreferences.edit()
                                .putString(Constants.KEY_JWT_TOKEN, null)
                                .putString(Constants.KEY_REFRESH_TOKEN, null)
                                .putString(Constants.KEY_USER_ID, "")
                                .apply()
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_REFRESH_TOKEN,
                                    null
                                )
                                } refresh token"
                            )
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_JWT_TOKEN,
                                    null
                                )
                                } access token"
                            )
                            Resource.Error(
                                uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun removeGameFavourite(id: Int): Resource<FavouriteRequest> {
        val request = IdRequest(id)
        try {
            val response = homeApi.gameRemoveFavourite(request)
            return if (response.isSuccessful) {
                Resource.Success(null)
            } else {
                coroutineScope {
                    val refreshToken = async { refreshingToken() }
                    val a = refreshToken.await()
                    println(a)
                    when (refreshToken.await()) {
                        is Resource.Success -> {
                            println("Здесь мы его поменяли")
                            addGameFavourite(id)
                        }
                        is Resource.Error -> {
                            println("Сюда ненада getEmail")
                            sharedPreferences.edit()
                                .putString(Constants.KEY_JWT_TOKEN, null)
                                .putString(Constants.KEY_REFRESH_TOKEN, null)
                                .putString(Constants.KEY_USER_ID, "")
                                .apply()
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_REFRESH_TOKEN,
                                    null
                                )
                                } refresh token"
                            )
                            println(
                                "${
                                sharedPreferences.getString(
                                    Constants.KEY_JWT_TOKEN,
                                    null
                                )
                                } access token"
                            )
                            Resource.Error(
                                uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun refreshingToken(): TokenResource {
        val refreshToken = sharedPreferences.getString(Constants.KEY_REFRESH_TOKEN, null)
        val request = TokenRequest(refreshToken)
        try {
            println("мы сюда вошли")
            val response = tokenRefreshApi.refreshAccessToken(request)
            return if (response.isSuccessful) {
                println("и сюда сюда вошли")
                sharedPreferences.edit()
                    .putString(Constants.KEY_JWT_TOKEN, response.body()?.access_token)
                    .putString(Constants.KEY_REFRESH_TOKEN, response.body()?.refresh_token)
                    .apply()
                println(
                    "${
                    sharedPreferences.getString(
                        Constants.KEY_REFRESH_TOKEN,
                        null
                    )
                    } refresh token"
                )
                println(
                    "${
                    sharedPreferences.getString(
                        Constants.KEY_JWT_TOKEN,
                        null
                    )
                    } access token"
                )
                Resource.Success(data = response.body())
            } else {
                println("и сюда вошли, но не надо было...")
                when {
                    response.code() == 403 -> {
                        Resource.Error(
                            uiText = UiText.StringResource(R.string.oops_something_went_wrong),
                            data = null
                        )
                    }
                    response.code() == 400 -> {
                        Resource.Error(
                            uiText = UiText.DynamicString("Не та ошибка"),
                            data = null
                        )
                    }
                    else -> {
                        Resource.Error(
                            uiText = UiText.DynamicString("Тоже не та ошибка"),
                            data = null
                        )
                    }
                }
            }
        } catch (e: IOException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server),
            )
        } catch (e: HttpException) {
            return Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }
}
