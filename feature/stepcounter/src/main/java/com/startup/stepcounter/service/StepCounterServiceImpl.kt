package com.startup.stepcounter.service

import android.content.Context
import android.content.Intent
import com.startup.common.util.OsVersions
import com.startup.domain.repository.local.StepDataStore
import com.startup.stepcounter.service.ServiceUtil.startForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StepCounterService {
    fun observeSteps(): Flow<Int>
    suspend fun resetSteps()
    fun startCounting()
    fun stopCounting()
}

class StepCounterServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepDataStore: StepDataStore
) : StepCounterService {
    override fun observeSteps(): Flow<Int> = stepDataStore.observeSteps()

    override fun startCounting() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            startForegroundService(context)
        } else {
            context.startService(Intent(context, WalkieStepForegroundService::class.java))
        }
    }

    override suspend fun resetSteps() {
        stepDataStore.resetSteps()
    }

    override fun stopCounting() {
        context.stopService(Intent(context, WalkieStepForegroundService::class.java))
    }
}