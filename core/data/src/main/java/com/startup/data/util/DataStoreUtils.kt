package com.startup.data.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val TOKEN_DATA_STORE_FILE_NAME = "walkie_token"

internal const val ACCESS_TOKEN_KEY_NAME = "AccessToken"
internal const val REFRESH_ACCESS_TOKEN_KEY_NAME = "RefreshToken"

internal val Context.userTokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_DATA_STORE_FILE_NAME)