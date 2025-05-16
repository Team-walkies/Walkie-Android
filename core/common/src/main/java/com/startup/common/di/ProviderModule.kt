package com.startup.common.di

import com.startup.common.provider.LocationProvider
import com.startup.common.provider.LocationProviderImpl
import com.startup.common.provider.ResourceProvider
import com.startup.common.provider.ResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {

    @Singleton
    @Binds
    abstract fun bindsStepDataStore(stepDataStore: LocationProviderImpl): LocationProvider

    @Singleton
    @Binds
    abstract fun bindsToResourceProvider(resourceProviderImpl: ResourceProviderImpl): ResourceProvider
}