package com.startup.domain.repository.local

import kotlinx.coroutines.flow.Flow

interface StepDataStore {
    suspend fun getCurrentSteps(): Int
    suspend fun saveCurrentSteps(steps: Int)
    suspend fun resetSteps()
    fun observeSteps(): Flow<Int>
}