package com.startup.stepcounter

import com.startup.stepcounter.service.StepCounterService
import com.startup.stepcounter.service.StepCounterServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StepCounterServiceModule {
    @Binds
    @Singleton
    abstract fun bindsStepCounterService(
        impl: StepCounterServiceImpl
    ): StepCounterService
}