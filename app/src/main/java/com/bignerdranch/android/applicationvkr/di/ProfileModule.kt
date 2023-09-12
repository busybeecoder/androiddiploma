package com.bignerdranch.android.applicationvkr.di

import android.content.SharedPreferences
import com.bignerdranch.android.applicationvkr.core.data.remote.TokenRefreshApi
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfileApi
import com.bignerdranch.android.applicationvkr.feature_profile.data.repository.ProfileRepositoryImpl
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository
import com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case.*
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
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApi(client: OkHttpClient): ProfileApi {
        return Retrofit.Builder()
            .baseUrl(ProfileApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileApi: ProfileApi,
        sharedPreferences: SharedPreferences,
        tokenRefreshApi: TokenRefreshApi
    ): ProfileRepository {
        return ProfileRepositoryImpl(profileApi, sharedPreferences, tokenRefreshApi)
    }

    @Provides
    @Singleton
    fun provideChangeEmailUseCase(repository: ProfileRepository): ChangeEmailUseCase {
        return ChangeEmailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetEmailUseCase(repository: ProfileRepository): GetEmailUseCase {
        return GetEmailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: ProfileRepository): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideChangePasswordUseCase(repository: ProfileRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavouritesUseCase(repository: ProfileRepository): GetFavouritesUseCase {
        return GetFavouritesUseCase(repository)
    }
}
