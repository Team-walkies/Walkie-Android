package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun getNotificationTodayStepEnabled(): Flow<Boolean>
    suspend fun updateNotificationTodayStepEnabled(enabled: Boolean)
    suspend fun getNotificationSpotArriveEnabled(): Flow<Boolean>
    suspend fun updateNotificationSpotArriveEnabled(enabled: Boolean)
    suspend fun getNotificationEggHatchEnabled(): Flow<Boolean>
    suspend fun updateNotificationEggHatchEnabled(enabled: Boolean)
    suspend fun getProfileAccessEnabled(): Flow<Boolean>
    suspend fun updateProfileAccessEnabled(enabled: Boolean)
}