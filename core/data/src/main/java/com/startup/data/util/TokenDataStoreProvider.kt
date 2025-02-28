package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface TokenDataStoreProvider {
    suspend fun <T> putValue(key: Preferences.Key<T>, value: T)
    suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): T
    fun <T> getFlowValue(key: Preferences.Key<T>): Flow<T>
    suspend fun clearAllData()
}