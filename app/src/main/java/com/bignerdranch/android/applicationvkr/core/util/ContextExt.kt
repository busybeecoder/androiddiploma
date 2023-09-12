package com.bignerdranch.android.applicationvkr.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openUrlInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    intent.setPackage("com.android.chrome")
    startActivity(intent)
}
