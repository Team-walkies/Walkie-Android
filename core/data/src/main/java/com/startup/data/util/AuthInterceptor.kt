package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.Printer
import com.startup.common.util.SessionExpireException
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
    private val authService: AuthService,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenDataStoreProvider.getValue(accessTokenKey, "")
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
                runCatching { authService.refreshTokenUpdate(BEARER + refreshToken) }.getOrNull()
            val data = refreshResponse?.data
            if (refreshResponse?.success != true || data == null) {
                throw SessionExpireException("세션 만료")
            } else {
                tokenDataStoreProvider.putValue(accessTokenKey, data.accessToken.orEmpty())
                tokenDataStoreProvider.putValue(refreshTokenKey, data.refreshToken.orEmpty())
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