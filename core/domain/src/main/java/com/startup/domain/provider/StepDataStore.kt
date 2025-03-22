package com.startup.domain.provider

import kotlinx.coroutines.flow.Flow

interface StepDataStore {
    suspend fun getCurrentSteps(): Int
    suspend fun saveCurrentSteps(steps: Int)
    suspend fun getTargetStep(): Int
    suspend fun setTargetStep(target: Int)
    suspend fun resetSteps()
    fun observeSteps(): Flow<Int>

    // 걸음수 초기화 관련 로직
    suspend fun isLastResetToday(): Boolean
    suspend fun saveLastResetDate()
    suspend fun saveYesterdaySteps(steps: Int)
    suspend fun getYesterdaySteps(): Int
}