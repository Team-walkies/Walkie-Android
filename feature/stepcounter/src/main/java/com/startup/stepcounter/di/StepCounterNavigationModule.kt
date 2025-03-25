package com.startup.stepcounter.di

import com.startup.navigation.StepCounterController
import com.startup.stepcounter.navigation.StepCounterControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StepCounterNavigationModule {

    @Singleton
    @Binds
    abstract fun bindsStepCounterController(controller: StepCounterControllerImpl): StepCounterController

}