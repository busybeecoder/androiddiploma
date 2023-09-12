package com.bignerdranch.android.applicationvkr.core.util

import com.bignerdranch.android.applicationvkr.core.data.remote.TokenResponse
import com.bignerdranch.android.applicationvkr.feature_auth.data.remote.ErrorType

typealias SimpleResource = Resource<Unit>
typealias ErrorResource = Resource<ErrorType?>
typealias TokenResource = Resource<TokenResponse?>

sealed class Resource<T>(val data: T? = null, val uiText: UiText? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(uiText: UiText? = null, data: T? = null) : Resource<T>(data, uiText)
}
