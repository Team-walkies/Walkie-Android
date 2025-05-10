package com.startup.stepcounter.service

import android.content.Context
import android.content.Intent
import com.startup.common.util.OsVersions
import com.startup.domain.provider.StepDataStore
import com.startup.stepcounter.service.ServiceUtil.startForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StepCounterService {
    fun observeSteps(): Flow<Pair<Int, Int>>
    suspend fun resetEggStep()
    fun startCounting()
    fun stopCounting()
    suspend fun checkAndResetForNewDay(): Int?  // null이면 오늘 이미 초기화됨, 아니면 이전 걸음 수 반환
}

class StepCounterServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepDataStore: StepDataStore
) : StepCounterService {
    override fun observeSteps(): Flow<Pair<Int, Int>> = stepDataStore.observeSteps()

    override fun startCounting() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            startForegroundService(context)
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
        // 이미 오늘 초기화했는지 확인
        if (stepDataStore.isLastResetToday()) {
            return null  // 이미 초기화됨
        }
        // 현재 걸음 수 가져오기
        val currentSteps = stepDataStore.getTodaySteps()
        // 걸음 수 초기화
        stepDataStore.resetTodaySteps()
        // 마지막 초기화 날짜 업데이트
        stepDataStore.saveLastResetDate()
        // 이전 걸음 수 반환
        return currentSteps
    }
}