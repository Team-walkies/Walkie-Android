package com.startup.stepcounter.service

import kotlinx.coroutines.flow.Flow

interface StepCounterService {
    fun observeSteps(): Flow<Pair<Int, Int>>
    suspend fun resetEggStep()
    fun startCounting()
    fun stopCounting()
    suspend fun checkAndResetForNewDay(): Int?  // null이면 오늘 이미 초기화됨, 아니면 이전 걸음 수 반환
}