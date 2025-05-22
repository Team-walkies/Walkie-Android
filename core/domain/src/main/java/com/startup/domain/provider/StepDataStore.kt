package com.startup.domain.provider

import kotlinx.coroutines.flow.Flow

interface StepDataStore {
    suspend fun getEggCurrentSteps(): Int
    suspend fun saveEggCurrentSteps(steps: Int)
    suspend fun getHatchingTargetStep(): Int
    suspend fun setHatchingTargetStep(target: Int)
    suspend fun getCurrentWalkEggId(): Long
    suspend fun setCurrentWalkEggId(eggId: Long)
    suspend fun resetHatchingSteps()
    suspend fun isTargetReached(): Boolean
    suspend fun setTargetReached(reached: Boolean)

    fun observeSteps(): Flow<Pair<Int, Int>>

    // 오늘 걸음수 관련 데이터
    suspend fun isLastResetToday(): Boolean
    suspend fun saveLastResetDate()
    suspend fun saveTodaySteps(steps: Int)
    suspend fun addTodaySteps(stepsToAdd: Int)
    suspend fun getTodaySteps(): Int
    suspend fun resetTodaySteps()
    suspend fun checkAndResetForNewDay(): Int?
}