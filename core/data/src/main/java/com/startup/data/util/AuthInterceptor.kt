package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.EMPTY_STRING
import com.startup.common.util.Printer
import com.startup.common.util.SessionManager
import com.startup.data.local.provider.LogoutManager
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.dto.request.auth.RefreshRequest
import com.startup.data.remote.service.AuthService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
internal class AuthInterceptor @Inject constructor(
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    private val sessionManager: SessionManager,
    private val logoutManager: LogoutManager,
    private val authService: AuthService,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : Interceptor {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenDataStoreProvider.getValue(
                accessTokenKey,
                EMPTY_STRING
            )
        }

        val request = chain.request()
            .newBuilder()
            .addHeaders(accessToken)
            .build()
        val response = chain.proceed(request)
        return if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Printer.e("LMH", "accessToken 만료 $accessToken")
            response.close()
            runBlocking(ioDispatcher) { refreshTokenUpdate(chain) }
        } else {
            response
        }
    }

    private suspend fun refreshTokenUpdate(
        chain: Interceptor.Chain,
    ): Response {
        val refreshToken =
            tokenDataStoreProvider.getValue(refreshTokenKey, "")
        val refreshResponse = runCatching { authService.refreshTokenUpdate(RefreshRequest(refreshToken)) }.getOrNull()
        val data = refreshResponse?.data
        if (refreshResponse?.status != 200 || data == null) {
            logoutManager.logout()
            sessionManager.notifySessionExpired()
            Printer.e("LMH", "refresh 토큰 세션 만료")
        } else {
            tokenDataStoreProvider.putValue(accessTokenKey, data.accessToken.orEmpty())
            tokenDataStoreProvider.putValue(refreshTokenKey, data.refreshToken.orEmpty())
            Printer.e("LMH", "토큰 재 발급")
        }

        val newToken = tokenDataStoreProvider.getValue(accessTokenKey, "")
        Printer.e("LMH", "refreshTokenUpdate $newToken")
        val newRequest = chain.request().newBuilder()
            .removeHeader("Authorization")
            .addHeaders(newToken)
            .build()
        val response = chain.proceed(newRequest)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            logoutManager.logout()
            sessionManager.notifySessionExpired()
            Printer.e("LMH", "refresh 토큰 세션 만료")
        }
        return response
    }

    companion object {
        private fun Request.Builder.addHeaders(token: String) =
            this.apply { header("Authorization", BEARER + token) }

        private const val BEARER = "Bearer "
    }
}