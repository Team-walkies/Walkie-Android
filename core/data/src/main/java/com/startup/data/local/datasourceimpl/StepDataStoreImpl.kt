package com.startup.data.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.startup.domain.provider.StepDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StepDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "walkie_settings")

    private companion object {
        private val TODAY_STEPS = intPreferencesKey("today_steps")
        private val CURRENT_STEPS = intPreferencesKey("current_steps")
        private val CURRENT_WALK_EGG_ID = longPreferencesKey("current_walk_egg_id")
        private val TARGET_STEP = intPreferencesKey("target_step")
        private val TARGET_REACHED = booleanPreferencesKey("target_reached")
        private val LAST_RESET_TIME = longPreferencesKey("last_reset_time")
        private val LAST_DAILY_API_CALL = longPreferencesKey("last_daily_api_call")
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

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply {
            timeInMillis = time1
        }
        val cal2 = Calendar.getInstance().apply {
            timeInMillis = time2
        }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    override suspend fun getEggCurrentSteps(): Int {
        return context.dataStore.data.first()[CURRENT_STEPS] ?: 0
    }

    override suspend fun saveEggCurrentSteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STEPS] = steps
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

        return cachedLastResetDayStart > 0 && isSameDay(cachedLastResetDayStart, cachedTodayStart)
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
        // 현재 걸음 수 가져오기
        val currentSteps = getTodaySteps()
        // 걸음 수 초기화
        resetTodaySteps()
        // 마지막 초기화 날짜 업데이트
        saveLastResetDate()
        // 이전 걸음 수 반환
        return currentSteps
    }

    override suspend fun shouldCallDailyApi(): Boolean {
        // 임시: 항상 true 반환 (테스트용)
        //return true
        
         val lastApiCall = context.dataStore.data.first()[LAST_DAILY_API_CALL] ?: 0L
         val currentTime = System.currentTimeMillis()
        // 한 번도 호출하지 않았거나, 마지막 호출이 오늘이 아닌 경우
         return lastApiCall == 0L || !isSameDay(lastApiCall, currentTime)
    }

    override suspend fun markDailyApiCalled() {
        val currentTime = System.currentTimeMillis()
        context.dataStore.edit { preferences ->
            preferences[LAST_DAILY_API_CALL] = currentTime
        }
    }
}