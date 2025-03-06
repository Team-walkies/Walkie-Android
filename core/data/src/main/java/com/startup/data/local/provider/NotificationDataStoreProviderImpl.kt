package com.startup.data.local.provider

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.startup.data.annotations.UserNotificationDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class NotificationDataStoreProviderImpl @Inject constructor(@UserNotificationDataStore private val preferencesDataStore: DataStore<Preferences>): NotificationDataStoreProvider {
    override suspend fun <T> putValue(key: Preferences.Key<T>, value: T) {
        preferencesDataStore.edit { it[key] = value }
    }

    override suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): T {
        return preferencesDataStore.data.map {
            it[key]
        }.firstOrNull() ?: defaultValue
    }

    override fun <T> getFlowValue(key: Preferences.Key<T>): Flow<T> {
        return preferencesDataStore.data.mapNotNull { it[key] }
    }

    override suspend fun clearAllData() {
        preferencesDataStore.edit { it.clear() }
    }
}