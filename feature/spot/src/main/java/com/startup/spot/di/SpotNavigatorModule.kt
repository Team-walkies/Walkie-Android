package com.startup.spot.di

import com.startup.navigation.SpotModuleNavigator
import com.startup.spot.navigation.SpotModuleNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SpotNavigatorModule {
    @Singleton
    @Binds
    abstract fun bindsSpotNavigator(navigator: SpotModuleNavigatorImpl): SpotModuleNavigator
}