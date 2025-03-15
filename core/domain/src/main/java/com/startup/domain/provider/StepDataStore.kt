package com.startup.domain.provider

import kotlinx.coroutines.flow.Flow

interface StepDataStore {
    suspend fun getCurrentSteps(): Int
    suspend fun saveCurrentSteps(steps: Int)
    suspend fun getTargetStep(): Int
    suspend fun setTargetStep(target: Int)
    suspend fun resetSteps()
    fun observeSteps(): Flow<Int>
}