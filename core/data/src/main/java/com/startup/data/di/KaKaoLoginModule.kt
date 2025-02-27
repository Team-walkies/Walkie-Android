package com.startup.data.di

import com.kakao.sdk.user.UserApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KaKaoLoginModule {

    @Provides
    @Singleton
    fun provideKaKaoLoginInstance(): UserApiClient {
        return UserApiClient.instance
    }
}