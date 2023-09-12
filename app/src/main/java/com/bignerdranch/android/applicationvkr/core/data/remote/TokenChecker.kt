package com.bignerdranch.android.applicationvkr.core.data.remote

// suspend fun <T> tokenChecker(
//    apiCall: suspend () -> Resource<T>
// ): Resource<T> {
//        try {
//            val response = TokenRefreshApi
//            Resource.Success(apiCall.invoke())
//        } catch (throwable: Throwable) {
//            when (throwable) {
//                is HttpException -> {
//                    Resource.Error(
//                        uiText = UiText.StringResource(R.string.oops_something_went_wrong)
//                    )
//                }
//                else -> {
//                    Resource.Error(
//                        uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
//                    )
//                }
//            }
//        }
//    }
// }
