package com.startup.data.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.startup.domain.provider.StepDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        private val TARGET_STEP = intPreferencesKey("target_step")
        private val TARGET_REACHED = booleanPreferencesKey("target_reached")
        private val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")
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
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastResetDate = context.dataStore.data.first()[LAST_RESET_DATE] ?: ""
        return today == lastResetDate
    }

    override suspend fun saveLastResetDate() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        context.dataStore.edit { preferences ->
            preferences[LAST_RESET_DATE] = today
        }
    }

    override suspend fun saveTodaySteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[TODAY_STEPS] = steps
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
}