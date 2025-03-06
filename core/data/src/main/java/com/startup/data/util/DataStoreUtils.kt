package com.startup.data.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val TOKEN_DATA_STORE_FILE_NAME = "walkie_token"
private const val NOTIFICATION_DATA_STORE_FILE_NAME = "walkie_notification"
private const val USER_SETTING_DATA_STORE_FILE_NAME = "walkie_user_setting"

internal const val ACCESS_TOKEN_KEY_NAME = "AccessToken"
internal const val REFRESH_ACCESS_TOKEN_KEY_NAME = "RefreshToken"

internal const val NOTIFICATION_TODAY_STEP_KEY_NAME = "NotificationTodayStep"
internal const val NOTIFICATION_SPOT_ARRIVE_KEY_NAME = "NotificationSpotArrive"
internal const val NOTIFICATION_EGG_HATCH_KEY_NAME = "NotificationEggHatch"

internal const val PROFILE_ACCESS_KEY_NAME = "ProfileAccess"

internal val Context.userTokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_DATA_STORE_FILE_NAME)

internal val Context.userNotificationDataStore: DataStore<Preferences> by preferencesDataStore(name = NOTIFICATION_DATA_STORE_FILE_NAME)

internal val Context.userSettingDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTING_DATA_STORE_FILE_NAME)