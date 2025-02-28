package com.startup.data.di

import com.startup.data.local.datasourceimpl.StepDataStoreImpl
import com.startup.data.util.TokenDataStoreProvider
import com.startup.data.util.TokenDataStoreProviderImpl
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
}