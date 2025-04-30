package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.EMPTY_STRING
import com.startup.common.util.Printer
import com.startup.common.util.SessionManager
import com.startup.data.local.provider.LogoutManager
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.dto.request.auth.RefreshRequest
import com.startup.data.remote.ext.addAuthorizationHeader
import com.startup.data.remote.service.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named

internal class TokenAuthenticator @Inject constructor(
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    private val sessionManager: SessionManager,
    private val logoutManager: LogoutManager,
    private val authService: AuthService,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : Authenticator {

    private val mutex = Mutex()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun authenticate(route: Route?, response: Response): Request? {
        // 이미 Authorization 헤더가 있는 경우 → 무한 루프 방지
        if (response.request.header("Authorization") != null && responseCount(response) >= 2) {
            runBlocking(ioDispatcher) {
                logoutManager.logout()
                sessionManager.notifySessionExpired()
                Printer.e("LMH", "재 시도 refresh 토큰 세션 만료")
            }
            return null
        }

        val newToken = runBlocking(ioDispatcher) {
            // 하나의 refresh만 허용
            mutex.withLock {
                runCatching {
                    val currentAccessToken = tokenDataStoreProvider.getValue(accessTokenKey, EMPTY_STRING)
                    val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

                    // 다른 요청이 먼저 토큰을 갱신했는지 확인
                    if (currentAccessToken != requestToken) {
                        Printer.e("LMH", "기존 토큰 반환")
                        return@withLock currentAccessToken
                    }
                    val refreshToken = tokenDataStoreProvider.getValue(refreshTokenKey, EMPTY_STRING)
                    val newAccessTokenResponse =
                        runCatching { authService.refreshTokenUpdate(RefreshRequest(refreshToken)).data }.getOrNull()

                    if (newAccessTokenResponse != null) {
                        tokenDataStoreProvider.putValue(accessTokenKey, newAccessTokenResponse.accessToken!!)
                        tokenDataStoreProvider.putValue(refreshTokenKey, newAccessTokenResponse.refreshToken!!)

                        Printer.e("LMH", "토큰 재 발급")
                        return@withLock newAccessTokenResponse.accessToken
                    } else {
                        throw NullPointerException()
                    }
                }.getOrElse {
                    Printer.e("LMH", "refresh 토큰 세션 만료")
                    logoutManager.logout()
                    sessionManager.notifySessionExpired()
                    return@withLock null
                }
            }
        }

        return newToken?.let {
            response.request.newBuilder()
                .removeHeader("Authorization")
                .addAuthorizationHeader(newToken)
                .build()
        } ?: run {
            Printer.e("LMH", "refresh 토큰 세션 만료")
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var res = response
        while (res.priorResponse != null) {
            count++
            res = res.priorResponse!!
        }
        return count
    }
}
