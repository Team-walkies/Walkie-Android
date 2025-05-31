package com.startup.domain.provider

import kotlinx.coroutines.flow.Flow

interface StepCounterService {
    fun observeSteps(): Flow<Pair<Int, Int>>
    fun startCounting()
    fun stopCounting()
    suspend fun resetEggStep()
    suspend fun checkAndResetForNewDay(): Int?
}