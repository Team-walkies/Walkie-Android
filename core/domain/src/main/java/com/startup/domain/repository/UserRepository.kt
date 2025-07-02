package com.startup.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getNotificationTodayStepEnabled(): Flow<Boolean>
    suspend fun updateNotificationTodayStepEnabled(enabled: Boolean)
    fun getNotificationSpotArriveEnabled(): Flow<Boolean>
    suspend fun updateNotificationSpotArriveEnabled(enabled: Boolean)
    fun getNotificationEggHatchEnabled(): Flow<Boolean>
    suspend fun updateNotificationEggHatchEnabled(enabled: Boolean)
    fun getProfileAccessEnabled(): Flow<Boolean>
    suspend fun updateProfileAccessEnabled(enabled: Boolean)
    suspend fun getMinAppVersionCode(): Long
    suspend fun isEggEventEnabled(): Boolean
}