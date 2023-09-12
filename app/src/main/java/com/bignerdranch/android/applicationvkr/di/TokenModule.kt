package com.bignerdranch.android.applicationvkr.di

import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRefreshApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {
    @Provides
    @Singleton
    fun provideProfileApi(client: OkHttpClient): TokenRefreshApi {
        return Retrofit.Builder()
            .baseUrl(TokenRefreshApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(TokenRefreshApi::class.java)
    }
}
