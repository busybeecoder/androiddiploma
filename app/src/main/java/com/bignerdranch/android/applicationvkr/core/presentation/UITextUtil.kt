package com.bignerdranch.android.applicationvkr.core.presentation

import android.content.Context
import com.bignerdranch.android.applicationvkr.core.util.UiText

fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.DynamicString -> this.value
        is UiText.StringResource -> context.getString(this.id)
    }
}
