package com.bignerdranch.android.applicationvkr.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            Constants.SHARED_PREF_NAME,
            MODE_PRIVATE
        )
    }

//    @Provides
//    @Singleton
//    fun provideAuthenticator(
//        api: TokenRefreshApi,
//        sharedPreferences: SharedPreferences
//    ): TokenAuthenticator {
//        return TokenAuthenticator(sharedPreferences = sharedPreferences, tokenApi = api)
//    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        sharedPreferences: SharedPreferences,
//        authenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val token = sharedPreferences.getString(Constants.KEY_JWT_TOKEN, null)
                println(token)
                val modifiedRequest = it.request().newBuilder()
                if (token != null) {
                    modifiedRequest.addHeader("Authorization", "Bearer $token")
                }
                it.proceed(modifiedRequest.build())
            }.also {
//                authenticator.let { client.authenticator(it) }
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }
//            .addInterceptor(
//                HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                }
//            )
            .build()
    }

    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader.Builder(app)
            .crossfade(true)
            .componentRegistry {
                add(SvgDecoder(app))
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
//        val gson = GsonBuilder()
//            .excludeFieldsWithModifiers(Modifier.STATIC)
//            .excludeFieldsWithModifiers(Modifier.PROTECTED)
//            .disableHtmlEscaping()
//            .create()
        return Gson()
    }
}
