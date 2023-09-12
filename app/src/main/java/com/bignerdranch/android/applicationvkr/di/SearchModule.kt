package com.bignerdranch.android.applicationvkr.di

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRefreshApi
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.HomeApi
import com.bignerdranch.android.applicationvkr.feature_search.data.repository.HomeRepositoryImpl
import com.bignerdranch.android.applicationvkr.feature_search.domain.repository.HomeRepository
import com.bignerdranch.android.applicationvkr.feature_search.domain.use_case.*
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
object SearchModule {

    @Provides
    @Singleton
    fun provideHomeApi(client: OkHttpClient): HomeApi {
        return Retrofit.Builder()
            .baseUrl(HomeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        homeApi: HomeApi,
        sharedPreferences: SharedPreferences,
        tokenRefreshApi: TokenRefreshApi
    ): HomeRepository {
        return HomeRepositoryImpl(homeApi, sharedPreferences, tokenRefreshApi)
    }

    @Provides
    @Singleton
    fun provideSetTagsUseCase(): SetTagsUseCase {
        return SetTagsUseCase()
    }

    @Provides
    @Singleton
    fun provideSearchUseCase(repository: HomeRepository): SearchUseCase {
        return SearchUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGameUseCase(repository: HomeRepository): GetGameUseCase {
        return GetGameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddFavouriteGameUseCase(repository: HomeRepository): AddFavouriteGameUseCase {
        return AddFavouriteGameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveFavouriteGameUseCase(repository: HomeRepository): RemoveFavouriteGameUseCase {
        return RemoveFavouriteGameUseCase(repository)
    }
}
