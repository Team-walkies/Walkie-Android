package com.startup.data.local.datasourceimpl

import androidx.datastore.preferences.core.Preferences
import com.startup.data.datasource.UserDataSource
import com.startup.data.local.provider.NotificationDataStoreProvider
import com.startup.data.local.provider.UserSettingDataStoreProvider
import com.startup.data.util.NOTIFICATION_EGG_HATCH_KEY_NAME
import com.startup.data.util.NOTIFICATION_SPOT_ARRIVE_KEY_NAME
import com.startup.data.util.NOTIFICATION_TODAY_STEP_KEY_NAME
import com.startup.data.util.PROFILE_ACCESS_KEY_NAME
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

internal class UserDataSourceImpl @Inject constructor(
    private val notificationDataStoreProvider: NotificationDataStoreProvider,
    private val userSettingDataStoreProvider: UserSettingDataStoreProvider,
    @Named(NOTIFICATION_TODAY_STEP_KEY_NAME) private val notificationTodayStepKey: Preferences.Key<Boolean>,
    @Named(NOTIFICATION_SPOT_ARRIVE_KEY_NAME) private val notificationSpotArriveKey: Preferences.Key<Boolean>,
    @Named(NOTIFICATION_EGG_HATCH_KEY_NAME) private val notificationEggHatchKey: Preferences.Key<Boolean>,
    @Named(PROFILE_ACCESS_KEY_NAME) private val profileAccessKey: Preferences.Key<Boolean>,
) : UserDataSource {
    override suspend fun getNotificationTodayStepEnabled(): Flow<Boolean> =
        notificationDataStoreProvider.getFlowValue(notificationTodayStepKey)

    override suspend fun updateNotificationTodayStepEnabled(enabled: Boolean) {
        notificationDataStoreProvider.putValue(notificationTodayStepKey, enabled)
    }

    override suspend fun getNotificationSpotArriveEnabled(): Flow<Boolean> =
        notificationDataStoreProvider.getFlowValue(notificationSpotArriveKey)

    override suspend fun updateNotificationSpotArriveEnabled(enabled: Boolean) {
        notificationDataStoreProvider.putValue(notificationSpotArriveKey, enabled)
    }

    override suspend fun getNotificationEggHatchEnabled(): Flow<Boolean> =
        notificationDataStoreProvider.getFlowValue(notificationEggHatchKey)

    override suspend fun updateNotificationEggHatchEnabled(enabled: Boolean) {
        notificationDataStoreProvider.putValue(notificationEggHatchKey, enabled)
    }

    override suspend fun getProfileAccessEnabled(): Flow<Boolean> =
        userSettingDataStoreProvider.getFlowValue(profileAccessKey)

    override suspend fun updateProfileAccessEnabled(enabled: Boolean) {
        userSettingDataStoreProvider.putValue(profileAccessKey, enabled)
    }
}