package com.startup.stepcounter.di

import com.startup.domain.provider.DateChangeNotifier
import com.startup.domain.provider.StepCounterService
import com.startup.stepcounter.broadcastReciver.DailyResetReceiver
import com.startup.stepcounter.service.StepCounterServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StepCounterModule {

    @Binds
    @Singleton
    abstract fun bindStepCounterService(
        stepCounterServiceImpl: StepCounterServiceImpl
    ): StepCounterService

    @Binds
    @Singleton
    abstract fun bindDateChangeNotifier(
        dailyResetReceiver: DailyResetReceiver
    ): DateChangeNotifier
}