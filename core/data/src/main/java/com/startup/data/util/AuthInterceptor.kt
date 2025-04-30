package com.startup.data.util

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.EMPTY_STRING
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.ext.addAuthorizationHeader
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
internal class AuthInterceptor @Inject constructor(
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            tokenDataStoreProvider.getValue(
                accessTokenKey,
                EMPTY_STRING
            )
        }

        val request = chain.request()
            .newBuilder()
            .addAuthorizationHeader(accessToken)
            .build()
        return chain.proceed(request)
    }
}