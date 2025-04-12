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
        private val CURRENT_STEPS = intPreferencesKey("current_steps")
        private val TARGET_STEP = intPreferencesKey("target_step")
        private val TARGET_REACHED = booleanPreferencesKey("target_reached")
        private val LAST_RESET_DATE = stringPreferencesKey("last_reset_date")
        private val YESTERDAY_STEPS = intPreferencesKey("yesterday_steps")
    }

    override suspend fun getCurrentSteps(): Int {
        return context.dataStore.data.first()[CURRENT_STEPS] ?: 0
    }

    override suspend fun saveCurrentSteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STEPS] = steps
        }
    }

    override suspend fun getTargetStep(): Int {
        return context.dataStore.data.first()[TARGET_STEP] ?: 10000
    }

    override suspend fun setTargetStep(target: Int) {
        context.dataStore.edit { preferences ->
            preferences[TARGET_STEP] = target
        }
    }

    override suspend fun resetSteps() {
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

    override fun observeSteps(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENT_STEPS] ?: 0
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

    override suspend fun saveYesterdaySteps(steps: Int) {
        context.dataStore.edit { preferences ->
            preferences[YESTERDAY_STEPS] = steps
        }
    }

    override suspend fun getYesterdaySteps(): Int {
        return context.dataStore.data.first()[YESTERDAY_STEPS] ?: 0
    }
}