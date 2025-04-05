package com.startup.data.di

import com.startup.data.local.datasourceimpl.StepDataStoreImpl
import com.startup.data.local.provider.LogoutManager
import com.startup.data.local.provider.LogoutManagerImpl
import com.startup.data.local.provider.NotificationDataStoreProvider
import com.startup.data.local.provider.NotificationDataStoreProviderImpl
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.local.provider.TokenDataStoreProviderImpl
import com.startup.data.local.provider.UserSettingDataStoreProvider
import com.startup.data.local.provider.UserSettingDataStoreProviderImpl
import com.startup.domain.provider.StepDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStoreModule {

    @Singleton
    @Binds
    abstract fun bindsStepDataStore(stepDataStore: StepDataStoreImpl): StepDataStore

    @Singleton
    @Binds
    abstract fun bindSettingPreferenceDataStoreProvider(tokenDataStoreProvider: TokenDataStoreProviderImpl): TokenDataStoreProvider

    @Singleton
    @Binds
    abstract fun bindNotificationPreferenceDataStoreProvider(notificationDataStoreProvider: NotificationDataStoreProviderImpl): NotificationDataStoreProvider

    @Singleton
    @Binds
    abstract fun bindUserSettingPreferenceDataStoreProvider(userSettingDataStoreProvider: UserSettingDataStoreProviderImpl): UserSettingDataStoreProvider

    @Singleton
    @Binds
    abstract fun bindLogoutManager(logoutManager: LogoutManagerImpl): LogoutManager
}