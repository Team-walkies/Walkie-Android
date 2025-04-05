package com.startup.data.local.provider

import javax.inject.Inject

class LogoutManagerImpl @Inject constructor(
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    private val notificationDataStoreProvider: NotificationDataStoreProvider,
    private val userSettingDataStoreProvider: UserSettingDataStoreProvider
) : LogoutManager {
    override suspend fun logout() {
        tokenDataStoreProvider.clearAllData()
        notificationDataStoreProvider.clearAllData()
        userSettingDataStoreProvider.clearAllData()
    }
}