package com.startup.login.di

import com.startup.navigation.LoginModuleNavigator
import com.startup.login.navigation.LoginModuleNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoginNavigatorModule {

    @Singleton
    @Binds
    abstract fun bindsLoginModuleNavigator(navigator: LoginModuleNavigatorImpl): LoginModuleNavigator
}