package com.startup.home.di

import com.startup.home.navigation.HomeModuleNavigatorImpl
import com.startup.navigation.HomeModuleNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HomeNavigationModule {
    @Singleton
    @Binds
    abstract fun bindsHomeModuleNavigator(navigator: HomeModuleNavigatorImpl): HomeModuleNavigator
}