package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.Printer
import com.startup.common.util.SessionExpireException
import com.startup.common.util.SessionManager
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.dto.request.auth.RefreshRequest
import com.startup.data.remote.service.AuthService
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
    private val authService: AuthService,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            // TODO defaultValue 제거
            tokenDataStoreProvider.getValue(accessTokenKey, "eyJhbGciOiJIUzI1NiJ9.eyJwcm92aWRlcklkIjoiMzk1ODI1NjA0MCIsIm1lbWJlcklkIjo2LCJpYXQiOjE3NDI5MDc2MjEsImV4cCI6MTc0NTQ5OTYyMX0.1VSczNwwq6jALTcJwW3qiotSb2PieySdGdPFjwjmDGI")
        }

        val request = chain.request()
            .newBuilder()
            .addHeaders(accessToken)
            .build()
        val response = chain.proceed(request)
        return if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Printer.e("LMH", "accessToken 만료 $accessToken")
            response.close()
            refreshTokenUpdate(chain)
        } else {
            response
        }
    }

    private fun refreshTokenUpdate(
        chain: Interceptor.Chain,
    ): Response {
        runBlocking {
            val refreshToken =
                tokenDataStoreProvider.getValue(refreshTokenKey, "")
            val refreshResponse =
                runCatching { authService.refreshTokenUpdate(RefreshRequest(refreshToken)) }.getOrNull()
            val data = refreshResponse?.data
            if (refreshResponse?.status != 200 || data == null) {
                sessionManager.notifySessionExpired()
                Printer.e("LMH", "refresh 토큰 세션 만료")
                throw SessionExpireException("세션 만료")
            } else {
                tokenDataStoreProvider.putValue(accessTokenKey, data.accessToken.orEmpty())
                tokenDataStoreProvider.putValue(refreshTokenKey, data.refreshToken.orEmpty())
                Printer.e("LMH", "토큰 재 발급")
            }
        }

        val newToken = runBlocking { tokenDataStoreProvider.getValue(accessTokenKey, "") }
        Printer.e("LMH", "refreshTokenUpdate $newToken")
        val newRequest = chain.request().newBuilder()
            .removeHeader("Authorization")
            .addHeaders(newToken)
            .build()
        val response = chain.proceed(newRequest)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            sessionManager.notifySessionExpired()
            Printer.e("LMH", "refresh 토큰 세션 만료")
            throw SessionExpireException("세션 만료")
        }
        return response
    }

    companion object {
        private fun Request.Builder.addHeaders(token: String) =
            this.apply { header("Authorization", BEARER + token) }

        private const val BEARER = "Bearer "
    }
}