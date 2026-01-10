package com.startup.data.remote.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.startup.common.util.DateUtil
import com.startup.common.util.Printer
import com.startup.data.remote.dto.request.healthcare.PutTodayWalkRequest
import com.startup.data.remote.service.HealthcareService
import com.startup.data.util.handleExceptionIfNeed
import com.startup.domain.provider.StepDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.round

@Singleton
class StepDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val healthcareService: HealthcareService,
) : StepDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "walkie_settings",
        corruptionHandler = ReplaceFileCorruptionHandler {
            emptyPreferences()
        }
    )

    private companion object {
        private val TODAY_STEPS = intPreferencesKey("today_steps")
        private val TODAY_WALK_TARGET_STEP = intPreferencesKey("today_walk_target_step")
        private val CURRENT_STEPS = intPreferencesKey("current_steps")
        private val CURRENT_WALK_EGG_ID = longPreferencesKey("current_walk_egg_id")
        private val TARGET_STEP = intPreferencesKey("target_step")
        private val TARGET_REACHED = booleanPreferencesKey("target_reached")
        private val LAST_RESET_TIME = longPreferencesKey("last_reset_time")
        private val LAST_DAILY_API_CALL = longPreferencesKey("last_daily_api_call")
        private val DAILY_GOAL_REACHED = booleanPreferencesKey("daily_goal_reached")
    }

    private var cachedLastResetDayStart: Long = 0  // 마지막 리셋 날짜의 자정 시간
    private var cachedTodayStart: Long = getTodayStartTime()

    private fun getTodayStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    override suspend fun getEggCurrentSteps(): Int {
        return context.dataStore.data.first()[CURRENT_STEPS] ?: 0
    }

    override suspend fun saveEggCurrentSteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STEPS] = steps
        }
    }

    override suspend fun getTodayWalkTargetStep(): Int {
        return context.dataStore.data.first()[TODAY_WALK_TARGET_STEP] ?: 6_000
    }

    override fun getTodayWalkTargetStepFlow(): Flow<Int> {
        return context.dataStore.data.map { it[TODAY_WALK_TARGET_STEP] ?: 6_000 }
    }

    override suspend fun saveTodayWalkTargetStep(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[TODAY_WALK_TARGET_STEP] = steps
        }
    }

    override suspend fun getHatchingTargetStep(): Int {
        return context.dataStore.data.first()[TARGET_STEP] ?: 0
    }

    override suspend fun setHatchingTargetStep(target: Int) {
        context.dataStore.edit { preferences ->
            preferences[TARGET_STEP] = target
        }
    }

    override suspend fun getCurrentWalkEggId(): Long {
        return context.dataStore.data.first()[CURRENT_WALK_EGG_ID] ?: 0
    }

    override suspend fun setCurrentWalkEggId(eggId: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_WALK_EGG_ID] = eggId
        }
    }

    override suspend fun resetHatchingSteps() {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STEPS] = 0
        }
    }

    override suspend fun isTargetReached(): Boolean {
        return context.dataStore.data.first()[TARGET_REACHED] ?: false
    }

    override suspend fun setTargetReached(reached: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TARGET_REACHED] = reached
        }
    }

    override suspend fun isLastResetToday(): Boolean {
        val todayStart = getTodayStartTime()

        if (todayStart != cachedTodayStart || cachedLastResetDayStart == 0L) {
            val lastResetTime = context.dataStore.data.first()[LAST_RESET_TIME] ?: 0L
            cachedLastResetDayStart = lastResetTime
            cachedTodayStart = todayStart
        }

        return cachedLastResetDayStart > 0 && DateUtil.isSameDay(cachedLastResetDayStart, cachedTodayStart)
    }

    override suspend fun saveLastResetDate() {
        val currentTimeMillis = System.currentTimeMillis()
        context.dataStore.edit { preferences ->
            preferences[LAST_RESET_TIME] = currentTimeMillis
        }
        cachedTodayStart = getTodayStartTime()
        cachedLastResetDayStart = cachedTodayStart
    }

    override suspend fun saveTodaySteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[TODAY_STEPS] = steps
        }
    }

    override suspend fun addTodaySteps(stepsToAdd: Int) {
        val current = getTodaySteps()
        context.dataStore.edit { preferences ->
            preferences[TODAY_STEPS] = current + stepsToAdd
        }
    }

    override suspend fun getTodaySteps(): Int {
        return context.dataStore.data.first()[TODAY_STEPS] ?: 0
    }

    override suspend fun resetTodaySteps() {
        context.dataStore.edit { preferences ->
            preferences[TODAY_STEPS] = 0
        }
    }

    /**
     * first : 알 걸음수 , second : 오늘의 걸음수
     */
    override fun observeSteps(): Flow<Pair<Int, Int>> = context.dataStore.data
        .map { preferences ->
            Pair(preferences[CURRENT_STEPS] ?: 0, preferences[TODAY_STEPS] ?: 0)
        }

    override suspend fun checkAndResetForNewDay(): Int? {
        // 이미 오늘 초기화했는지 확인
        if (isLastResetToday()) {
            return null  // 이미 초기화됨
        }

        // 최초 실행 등으로 마지막 리셋 시간이 비어있을 수 있음
        val lastResetTime = context.dataStore.data.first()[LAST_RESET_TIME] ?: 0L

        if (lastResetTime == 0L) {
            // 기준점을 오늘로 설정만 하고 네트워크/리셋은 수행하지 않음
            saveLastResetDate()
            return null
        }

        // 직전 일자의 누적 걸음 수를 서버로 전송 후 리셋
        val currentSteps = getTodaySteps()
        putHealthcareInfo(lastResetTime, currentSteps)
        // 일일 목표 달성 여부도 리셋
        setDailyGoalReached(false)
        // 이전 걸음 수 반환
        return currentSteps
    }


    private val mutex = Mutex()


    private suspend fun putHealthcareInfo(targetDate: Long, currentSteps: Int) {
        mutex.withLock {
            val isSameDay = DateUtil.isSameDay(targetDate, System.currentTimeMillis())
            try {
                if (!isSameDay) {
                    val distanceMeters = (round((0.0006 * currentSteps) * 100) / 100.0)
                    val calories = currentSteps / 30
                    val targetDateStr = DateUtil.formatDateModern(targetDate)
                    Printer.e(
                        "LMH",
                        "걸음 수 업데이트\nstep : $currentSteps\nday : $targetDateStr\ntoday : ${DateUtil.formatDateModern(System.currentTimeMillis())}"
                    )
                    handleExceptionIfNeed {
                        healthcareService.putHealthcareInfo(
                            PutTodayWalkRequest(
                                nowCalories = calories,
                                nowDistance = distanceMeters,
                                nowSteps = currentSteps,
                                nowDay = targetDateStr,
                                targetSteps = getTodayWalkTargetStep(),
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Printer.e("LMH", "ERROR $e")
            } finally {
                // 동일 일자일 경우에는 리셋은 하지 않되 기준점은 업데이트
                if (!isSameDay) {
                    resetTodaySteps()
                }
                saveLastResetDate()
            }
        }
    }

    override suspend fun shouldCallDailyApi(): Boolean {
        // 임시: 항상 true 반환 (테스트용)
        //return true

        val lastApiCall = context.dataStore.data.first()[LAST_DAILY_API_CALL] ?: 0L
        val currentTime = System.currentTimeMillis()
        // 한 번도 호출하지 않았거나, 마지막 호출이 오늘이 아닌 경우
        return lastApiCall == 0L || !DateUtil.isSameDay(lastApiCall, currentTime)
    }

    override suspend fun markDailyApiCalled() {
        val currentTime = System.currentTimeMillis()
        context.dataStore.edit { preferences ->
            preferences[LAST_DAILY_API_CALL] = currentTime
        }
    }

    override suspend fun isDailyGoalReached(): Boolean {
        return context.dataStore.data.first()[DAILY_GOAL_REACHED] ?: false
    }

    override suspend fun setDailyGoalReached(reached: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_GOAL_REACHED] = reached
        }
    }
}