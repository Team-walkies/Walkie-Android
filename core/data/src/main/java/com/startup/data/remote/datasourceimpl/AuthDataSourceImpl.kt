package com.startup.data.remote.datasourceimpl

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.KakaoAuthFailException
import com.startup.common.util.ResponseErrorException
import com.startup.data.datasource.AuthDataSource
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.ext.requireNotNull
import com.startup.data.remote.service.AuthService
import com.startup.data.remote.service.MemberService
import com.startup.data.util.ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.KaKaoLoginClient
import com.startup.data.util.REFRESH_ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.TokenDataStoreProvider
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject
import javax.inject.Named

internal class AuthDataSourceImpl @Inject constructor(
    private val kaKaoLoginClient: KaKaoLoginClient,
    private val authService: AuthService,
    private val memberService: MemberService,
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : AuthDataSource {
    override fun kakaoLogin(): Flow<String> = flow {
        kaKaoLoginClient.login().fold(
            onSuccess = { token ->
                emit(token.accessToken)
            },
            onFailure = { error ->
                throw KakaoAuthFailException("로그인 오류 : $error")
            }
        )
    }

    override fun login(): Flow<Unit> = callbackFlow {
        kakaoLogin().map {
            val item = authService.login(it)
            if (item.success != true) {
                throw ResponseErrorException(item.message.orEmpty())
            }
            item.data.requireNotNull()
        }.onEmpty {
            throw KakaoAuthFailException("로그인 오류")
        }.catch {
            throw KakaoAuthFailException("로그인 오류")
        }.onEach {
            tokenDataStoreProvider.putValue(accessTokenKey, it.accessToken!!)
            tokenDataStoreProvider.putValue(refreshTokenKey, it.refreshToken!!)
            send(Unit)
        }.catch {
            throw ResponseErrorException("로그인 오류")
        }
    }

    override fun unLink(): Flow<Unit> = flow {
        handleExceptionIfNeed {
            kaKaoLoginClient.unLink()
            emitRemote(memberService.withdrawalService())
        }
    }

    override fun logOut(): Flow<Unit> = flow {
        handleExceptionIfNeed {
            kaKaoLoginClient.logout()
            emitRemote(memberService.logout())
        }
    }
}