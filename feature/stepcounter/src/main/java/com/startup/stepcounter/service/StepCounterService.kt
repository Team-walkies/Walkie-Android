package com.startup.stepcounter.service

import kotlinx.coroutines.flow.Flow

interface StepCounterService {
    fun observeSteps(): Flow<Pair<Int, Int>>
    suspend fun resetEggStep()
    fun startCounting()
    fun stopCounting()
    suspend fun checkAndResetForNewDay(): Int?
}