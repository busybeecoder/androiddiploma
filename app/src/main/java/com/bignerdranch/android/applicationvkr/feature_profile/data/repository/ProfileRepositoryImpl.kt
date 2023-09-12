package com.bignerdranch.android.applicationvkr.feature_profile.data.repository

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRefreshApi
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRequest
import com.bignerdranch.android.applicationvkr.core.util.*
import com.bignerdranch.android.applicationvkr.feature_auth.data.repository.ErrorResponse
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ChangeEmailRequest
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfileApi
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfileEmailRequest
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfilePasswordRequest
import com.bignerdranch.android.applicationvkr.feature_profile.domain.models.Profile
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val sharedPreferences: SharedPreferences,
    private val tokenRefreshApi: TokenRefreshApi
) : ProfileRepository {
    private val gson = Gson()
    private val type = object : TypeToken<ErrorResponse>() {}.type
    override suspend fun getProfile(userId: String): Resource<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): SimpleResource {
        return try {
            profileApi.logout()
            println("логаут делаем ебана")
            sharedPreferences.edit()
                .putString(Constants.KEY_JWT_TOKEN, null)
                .putString(Constants.KEY_REFRESH_TOKEN, null)
                .putString(Constants.KEY_USER_ID, "")
                .apply()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        }
    }

    override suspend fun getEmail(): Resource<ProfileEmailRequest> {
        try {
            val response = profileApi.getEmail()
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
                            getEmail()
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

    override suspend fun changeEmail(
        email: String
    ): SimpleResource {
        val request = ChangeEmailRequest(email)
        return try {
            val response = profileApi.changeEmail(request)
            if (response.isSuccessful) {
                println("ээээээээй")
                if (response.body()?.error != null) {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.wrong_email_error)
                    )
                } else {
                    println("чегоооо")
                    val token = response.body()
                    println(sharedPreferences)
                    sharedPreferences.edit()
                        .putString(Constants.KEY_JWT_TOKEN, token?.access_token)
                        .putString(Constants.KEY_REFRESH_TOKEN, token?.refresh_token)
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
                    Resource.Success(Unit)
                }
            } else {
                if (response.code() == 403) {
                    coroutineScope {
                        val refreshToken = async { refreshingToken() }
                        when (refreshToken.await()) {
                            is Resource.Success -> {
                                println("Здесь мы его поменяли")
                                changeEmail(email)
                            }
                            is Resource.Error -> {
                                println("Сюда ненада change email")
                                sharedPreferences.edit()
                                    .putString(Constants.KEY_JWT_TOKEN, null)
                                    .putString(Constants.KEY_REFRESH_TOKEN, null)
                                    .putString(Constants.KEY_USER_ID, "")
                                Resource.Error(
                                    uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                                )
                            }
                        }
                    }
                } else {
                    val errorBody = response.errorBody()
                    val errorResponse: ErrorResponse? = gson.fromJson(errorBody?.charStream(), type)
                    println(errorResponse?.error)
                    val errorResponseMessage = errorResponse?.error?.get(0).toString()
                    if (errorResponseMessage == "Wrong data format") {
                        Resource.Error(
                            uiText = UiText.DynamicString(errorResponseMessage)
                        )
                    } else {
                        Resource.Error(
                            uiText = UiText.StringResource(R.string.oops_something_went_wrong)
                        )
                    }
                }
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    // TODO change Response in API, change error handling, check is everything is alright

    override suspend fun changePassword(
        current_password: String,
        new_password: String
    ): SimpleResource {
        val request = ProfilePasswordRequest(current_password, new_password)
        return try {
            val response = profileApi.changePassword(request)
            if (response.isSuccessful) {
                if (response.body()?.error != null) {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.wrong_old_password)
                    )
                } else {
                    val token = response.body()
                    println(sharedPreferences)
                    sharedPreferences.edit()
                        .putString(Constants.KEY_JWT_TOKEN, token?.access_token)
                        .putString(Constants.KEY_REFRESH_TOKEN, token?.refresh_token)
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
                    Resource.Success(Unit)
                }
            } else {
                if (response.code() == 403) {
                    coroutineScope {
                        val refreshToken = async { refreshingToken() }
                        when (refreshToken.await()) {
                            is Resource.Success -> {
                                println("Здесь мы его поменяли")
                                changePassword(current_password, new_password)
                            }
                            is Resource.Error -> {
                                println("Сюда ненада change password")
                                sharedPreferences.edit()
                                    .putString(Constants.KEY_JWT_TOKEN, null)
                                    .putString(Constants.KEY_REFRESH_TOKEN, null)
                                    .putString(Constants.KEY_USER_ID, "")
                                Resource.Error(
                                    uiText = UiText.DynamicString("Истек срок действия токена. Перезайдите в приложение."),
                                )
                            }
                        }
                    }
                } else {
                    val errorBody = response.errorBody()
                    val errorResponse: ErrorResponse? = gson.fromJson(errorBody?.charStream(), type)
                    println(errorResponse?.error)
                    val errorResponseMessage = errorResponse?.error?.get(0).toString()
                    if (errorResponseMessage == "Wrong data format") {
                        Resource.Error(
                            uiText = UiText.DynamicString(errorResponseMessage)
                        )
                    } else {
                        Resource.Error(
                            uiText = UiText.StringResource(R.string.oops_something_went_wrong)
                        )
                    }
                }
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun getFavourites(): Resource<List<GameRequest>> {
        try {
            val response = profileApi.getFavourites()
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
                            getFavourites()
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
