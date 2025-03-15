package com.startup.data.local.datasourceimpl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.startup.domain.provider.StepDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    override fun observeSteps(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENT_STEPS] ?: 0
        }
}