package com.startup.walkie.di

import com.startup.navigation.HomeFeatureNavigator
import com.startup.navigation.LoginFeatureNavigator
import com.startup.navigation.AuthFlowNavigator
import com.startup.navigation.StepCounterController
import com.startup.walkie.navigationimpl.HomeFeatureNavigatorImpl
import com.startup.walkie.navigationimpl.LoginFeatureNavigatorImpl
import com.startup.walkie.navigationimpl.AuthFlowNavigatorImpl
import com.startup.walkie.navigationimpl.StepCounterControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Singleton
    @Binds
    abstract fun bindsHomeFeatureNavigator(navigator: HomeFeatureNavigatorImpl): HomeFeatureNavigator

    @Singleton
    @Binds
    abstract fun bindsLoginFeatureNavigator(navigator: LoginFeatureNavigatorImpl): LoginFeatureNavigator

    @Singleton
    @Binds
    abstract fun bindsLoginMoveNavigator(navigator: AuthFlowNavigatorImpl): AuthFlowNavigator

    @Singleton
    @Binds
    abstract fun bindsStepCounterController(controller: StepCounterControllerImpl): StepCounterController

}