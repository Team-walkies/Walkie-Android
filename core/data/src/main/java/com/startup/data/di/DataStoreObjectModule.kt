package com.startup.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.startup.data.annotations.UserNotificationDataStore
import com.startup.data.annotations.UserSettingDataStore
import com.startup.data.annotations.UserTokenDataStore
import com.startup.data.util.ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.NOTIFICATION_EGG_HATCH_KEY_NAME
import com.startup.data.util.NOTIFICATION_SPOT_ARRIVE_KEY_NAME
import com.startup.data.util.NOTIFICATION_TODAY_STEP_KEY_NAME
import com.startup.data.util.PROFILE_ACCESS_KEY_NAME
import com.startup.data.util.REFRESH_ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.userNotificationDataStore
import com.startup.data.util.userSettingDataStore
import com.startup.data.util.userTokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreObjectModule {

    @Provides
    @UserTokenDataStore
    fun provideUserTokenPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.userTokenDataStore

    @Provides
    @UserNotificationDataStore
    fun provideUserNotificationPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.userNotificationDataStore

    @Provides
    @UserSettingDataStore
    fun provideUserSettingPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.userSettingDataStore

    @Provides
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME)
    fun providesAccessTokenKey(): Preferences.Key<String> =
        stringPreferencesKey(REFRESH_ACCESS_TOKEN_KEY_NAME)

    @Provides
    @Named(ACCESS_TOKEN_KEY_NAME)
    fun providesRefreshTokenKey(): Preferences.Key<String> =
        stringPreferencesKey(ACCESS_TOKEN_KEY_NAME)

    @Provides
    @Named(NOTIFICATION_TODAY_STEP_KEY_NAME)
    fun providesNotificationTodayStepKey(): Preferences.Key<Boolean> =
        booleanPreferencesKey(NOTIFICATION_TODAY_STEP_KEY_NAME)

    @Provides
    @Named(NOTIFICATION_SPOT_ARRIVE_KEY_NAME)
    fun providesNotificationSpotArriveKey(): Preferences.Key<Boolean> =
        booleanPreferencesKey(NOTIFICATION_SPOT_ARRIVE_KEY_NAME)

    @Provides
    @Named(NOTIFICATION_EGG_HATCH_KEY_NAME)
    fun providesNotificationEggHatchKey(): Preferences.Key<Boolean> =
        booleanPreferencesKey(NOTIFICATION_EGG_HATCH_KEY_NAME)

    @Provides
    @Named(PROFILE_ACCESS_KEY_NAME)
    fun providesProfileAccessKey(): Preferences.Key<Boolean> =
        booleanPreferencesKey(PROFILE_ACCESS_KEY_NAME)
}