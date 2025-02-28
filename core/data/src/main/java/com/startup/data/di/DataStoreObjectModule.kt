package com.startup.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.startup.data.annotations.UserTokenDataStore
import com.startup.data.util.ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.REFRESH_ACCESS_TOKEN_KEY_NAME
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
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME)
    fun providesAccessTokenKey(): Preferences.Key<String> = stringPreferencesKey(REFRESH_ACCESS_TOKEN_KEY_NAME)

    @Provides
    @Named(ACCESS_TOKEN_KEY_NAME)
    fun providesRefreshTokenKey(): Preferences.Key<String> = stringPreferencesKey(ACCESS_TOKEN_KEY_NAME)
}