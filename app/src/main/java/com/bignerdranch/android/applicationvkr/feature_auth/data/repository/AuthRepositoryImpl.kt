package com.bignerdranch.android.applicationvkr.feature_auth.data.repository

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.util.*
import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.AuthApi
import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.CreateAccountRequest
import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.LoginRequest
import com.bignerdranch.android.applicationvkr.feature_auth.domain.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {
    private val gson = Gson()
    private val type = object : TypeToken<ErrorResponse>() {}.type

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): ErrorResource {

        val request = CreateAccountRequest(email, username, password)
        try {
            val response = api.register(request)
            return if (response.isSuccessful) {
                if (response.body()?.error?.username != null || response.body()?.error?.email != null) {
                    Resource.Error(
                        data = response.body()?.error
                    )
                } else {
                    Resource.Success(null)
                }
            } else {
                val errorBody = response.errorBody()
                val errorResponse: ErrorResponse? = gson.fromJson(errorBody?.charStream(), type)
                println(errorResponse?.error?.get(0))
                val errorResponseMessage = errorResponse?.error
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

    override suspend fun login(username: String, password: String): SimpleResource {
        val request = LoginRequest(username, password)
        return try {
            val response = api.login(request)
            println(response.toString())
            println(response.code())
            println("${response.body()} responsebody")
            println("${response.body()?.error} responsebody")
            if (response.isSuccessful) {
                if (response.body()?.error != null) {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.wrong_login)
                    )
                } else {
                    val token = response.body()
                    println(sharedPreferences)
                    sharedPreferences.edit()
                        .putString(Constants.KEY_JWT_TOKEN, token?.access_token)
                        .putString(Constants.KEY_REFRESH_TOKEN, token?.refresh_token)
                        .apply()
                    println(sharedPreferences.getString(Constants.KEY_REFRESH_TOKEN, null))
                    println("aaaaaaaaaaaaaa")
                    println(sharedPreferences.getString(Constants.KEY_JWT_TOKEN, null))
                    Resource.Success(Unit)
                }
            } else {
                val errorBody = response.errorBody()
                val errorResponse: ErrorResponse? = gson.fromJson(errorBody?.charStream(), type)
                println(errorResponse?.error?.get(0))
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
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        }
    }
}

data class ErrorResponse(
    @SerializedName("error")
    val error: String
)
