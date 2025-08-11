package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getNotificationTodayStepEnabled(): Flow<Boolean>
    suspend fun updateNotificationTodayStepEnabled(enabled: Boolean)
    fun getNotificationSpotArriveEnabled(): Flow<Boolean>
    suspend fun updateNotificationSpotArriveEnabled(enabled: Boolean)
    fun getNotificationEggHatchEnabled(): Flow<Boolean>
    suspend fun updateNotificationEggHatchEnabled(enabled: Boolean)
    fun getProfileAccessEnabled(): Flow<Boolean>
    suspend fun updateProfileAccessEnabled(enabled: Boolean)
    suspend fun isHealthcareBottomSheetShown(): Boolean
    suspend fun setHealthcareBottomSheetShown(shown: Boolean)
}