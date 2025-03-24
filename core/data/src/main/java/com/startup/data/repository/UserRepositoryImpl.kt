package com.startup.data.repository

import com.startup.data.datasource.UserDataSource
import com.startup.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override fun getNotificationTodayStepEnabled(): Flow<Boolean> =
        userDataSource.getNotificationTodayStepEnabled()

    override suspend fun updateNotificationTodayStepEnabled(enabled: Boolean) {
        userDataSource.updateNotificationTodayStepEnabled(enabled)
    }

    override fun getNotificationSpotArriveEnabled() =
        userDataSource.getNotificationSpotArriveEnabled()

    override suspend fun updateNotificationSpotArriveEnabled(enabled: Boolean) {
        userDataSource.updateNotificationSpotArriveEnabled(enabled)
    }

    override fun getNotificationEggHatchEnabled() =
        userDataSource.getNotificationEggHatchEnabled()

    override suspend fun updateNotificationEggHatchEnabled(enabled: Boolean) {
        userDataSource.updateNotificationEggHatchEnabled(enabled)
    }

    override fun getProfileAccessEnabled() = userDataSource.getProfileAccessEnabled()

    override suspend fun updateProfileAccessEnabled(enabled: Boolean) {
        userDataSource.updateProfileAccessEnabled(enabled)
    }

}