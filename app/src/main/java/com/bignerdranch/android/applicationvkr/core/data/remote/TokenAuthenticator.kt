package com.bignerdranch.android.applicationvkr.core.data.remote

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.core.util.UiText
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

//class TokenAuthenticator @Inject constructor(
//    private val tokenApi: TokenRefreshApi,
//    val sharedPreferences: SharedPreferences
//) : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        return runBlocking {
//            when (val tokenResponse = getUpdatedToken()) {
//                is Resource.Success -> {
//                    sharedPreferences.edit()
//                        .putString(Constants.KEY_JWT_TOKEN, tokenResponse.data?.access_token)
//                        .putString(Constants.KEY_REFRESH_TOKEN, tokenResponse.data?.refresh_token)
//                        .apply()
//                    response.request.newBuilder()
//                        .header("Authorization", "Bearer ${tokenResponse.data?.access_token}")
//                        .build()
//                }
//                else -> null
//            }
//        }
//    }
//
//    private suspend fun getUpdatedToken(): Resource<TokenResponse> {
//        val refreshToken = sharedPreferences.getString(Constants.KEY_REFRESH_TOKEN, null)
//        val response = tokenApi.refreshAccessToken(refreshToken)
//        return if (response.isSuccessful) {
//            Resource.Success(data = response.body())
//        } else {
//            Resource.Error(
//                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
//            )
//        }
//    }
//}
