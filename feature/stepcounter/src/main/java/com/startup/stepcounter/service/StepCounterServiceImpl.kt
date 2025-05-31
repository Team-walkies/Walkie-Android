package com.startup.stepcounter.service

import android.content.Context
import android.content.Intent
import com.startup.common.util.OsVersions
import com.startup.domain.provider.StepCounterService
import com.startup.domain.provider.StepDataStore
import com.startup.stepcounter.service.ServiceUtil.startStepTracking
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepCounterServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepDataStore: StepDataStore
) : StepCounterService {
    override fun observeSteps(): Flow<Pair<Int, Int>> = stepDataStore.observeSteps()

    override fun startCounting() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            startStepTracking(context)
        } else {
            context.startService(Intent(context, WalkieStepForegroundService::class.java))
        }
    }

    override suspend fun resetEggStep() {
        stepDataStore.resetHatchingSteps()
        stepDataStore.setHatchingTargetStep(0)
        stepDataStore.setTargetReached(false)
    }

    override fun stopCounting() {
        context.stopService(Intent(context, WalkieStepForegroundService::class.java))
    }

    override suspend fun checkAndResetForNewDay(): Int? {
        return stepDataStore.checkAndResetForNewDay()
    }
}