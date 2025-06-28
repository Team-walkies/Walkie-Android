package com.startup.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.startup.common.util.Printer
import com.startup.data.BuildConfig
import com.startup.data.datasource.UserDataSource
import com.startup.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.resume

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val remoteConfig: FirebaseRemoteConfig
) :
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

    override suspend fun getMinAppVersionCode() = withContext(Dispatchers.IO) {
        withTimeout(3000) { // 3초 안에 fetch 못 하면 TimeoutCancellationException 발생 하도록
            suspendCancellableCoroutine { cont ->
                if (BuildConfig.DEBUG) {
                    cont.resume(0L)
                } else {
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener { task ->
                            val result = runCatching {
                                if (task.isSuccessful) {
                                    val value = remoteConfig.getLong(KEY_MIN_APP_VERSION)
                                    Printer.d("LMH", "RemoteConfig minAppVersion: $value")
                                    value
                                } else {
                                    Printer.e("LMH", "RemoteConfig 실패: ${task.exception}")
                                    0L
                                }
                            }.getOrElse {
                                Printer.e("LMH", "RemoteConfig 예외: $it")
                                0L
                            }
                            if (cont.isActive) cont.resume(result)
                        }
                }
            }
        }
    }

    override suspend fun isEggEventEnabled() = withContext(Dispatchers.IO) {
        withTimeout(3000) {
            suspendCancellableCoroutine { cont ->
                if (BuildConfig.DEBUG) {
                    cont.resume(true) // 디버그에서는 기본적으로 활성화
                } else {
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener { task ->
                            val result = runCatching {
                                if (task.isSuccessful) {
                                    val value = remoteConfig.getBoolean(KEY_EGG_EVENT_ENABLED)
                                    Printer.d("JUNWOO", "RemoteConfig eggEventEnabled: $value")
                                    value
                                } else {
                                    Printer.e("JUNWOO", "RemoteConfig 실패: ${task.exception}")
                                    false
                                }
                            }.getOrElse {
                                Printer.e("JUNWOO", "RemoteConfig 예외: $it")
                                false
                            }
                            if (cont.isActive) cont.resume(result)
                        }
                }
            }
        }
    }

    companion object {
        private const val KEY_MIN_APP_VERSION = "AOS_MIN_APP_VERSION"
        private const val KEY_EGG_EVENT_ENABLED = "EGG_EVENT_ENABLED"
    }
}