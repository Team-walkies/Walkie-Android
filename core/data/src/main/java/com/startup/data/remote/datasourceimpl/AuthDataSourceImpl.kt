package com.startup.data.remote.datasourceimpl

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.KakaoAuthFailException
import com.startup.common.util.ResponseErrorException
import com.startup.common.util.UserAuthNotFoundException
import com.startup.data.datasource.AuthDataSource
import com.startup.data.local.provider.LogoutManager
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.dto.request.auth.JoinRequest
import com.startup.data.remote.dto.request.auth.LoginRequest
import com.startup.data.remote.dto.response.auth.TokenDto
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.ext.requireNotNull
import com.startup.data.remote.service.AuthService
import com.startup.data.remote.service.MemberService
import com.startup.data.util.ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.KakaoLoginClient
import com.startup.data.util.REFRESH_ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject
import javax.inject.Named

internal class AuthDataSourceImpl @Inject constructor(
    private val kakaoLoginClient: KakaoLoginClient,
    private val authService: AuthService,
    private val memberService: MemberService,
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    private val logoutManager: LogoutManager,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : AuthDataSource {
    override fun kakaoLogin(): Flow<String> = flow {
        kakaoLoginClient.login().fold(
            onSuccess = { token ->
                emit(token.accessToken)
            },
            onFailure = { error ->
                throw KakaoAuthFailException("로그인 오류 : $error")
            }
        )
    }

    override fun loginWithKakao(kakaoAccessToken: String): Flow<Unit> = flow<TokenDto> {
        emitRemote(
            authService.login(
                LoginRequest(
                    provider = "kakao",
                    loginAccessToken = kakaoAccessToken
                )
            )
        )
    }.map {
        tokenDataStoreProvider.putValue(accessTokenKey, it.accessToken!!)
        tokenDataStoreProvider.putValue(refreshTokenKey, it.refreshToken!!)
        Unit
    }.catch {
        if (it is ResponseErrorException) {
            throw UserAuthNotFoundException(
                message = it.message,
                providerToken = kakaoAccessToken
            )
        }
    }

    override fun login(): Flow<Unit> = callbackFlow {
        kakaoLogin().onEmpty {
            throw KakaoAuthFailException("로그인 오류")
        }.map {
            val item = authService.login(LoginRequest(provider = "kakao", loginAccessToken = it))
            if (item.status == 204) {
                throw UserAuthNotFoundException(
                    message = item.message.orEmpty(),
                    providerToken = it
                )
            }
            item.data.requireNotNull()
        }.onEach {
            tokenDataStoreProvider.putValue(accessTokenKey, it.accessToken!!)
            tokenDataStoreProvider.putValue(refreshTokenKey, it.refreshToken!!)
            send(Unit)
        }.catch {
            throw ResponseErrorException("로그인 오류")
        }.launchIn(this)
    }

    override fun unLink(): Flow<Unit> = flow {
        handleExceptionIfNeed {
            kakaoLoginClient.unLink()
            emitRemote(memberService.withdrawalService())
            logoutManager.logout()
        }
    }

    override fun logOut(): Flow<Unit> = flow {
        handleExceptionIfNeed {
            kakaoLoginClient.logout()
            emitRemote(memberService.logout())
            logoutManager.logout()
        }
    }

    override fun localLogout(): Flow<Unit> = flow {
        logoutManager.logout()
        emit(Unit)
    }

    override fun joinWalkie(providerToken: String, nickName: String): Flow<Unit> = flow {
        emitRemote(
            authService.join(
                JoinRequest(
                    provider = "kakao",
                    loginAccessToken = providerToken,
                    nickName = nickName
                )
            )
        )
    }.map {
        tokenDataStoreProvider.putValue(accessTokenKey, it.accessToken!!)
        tokenDataStoreProvider.putValue(refreshTokenKey, it.refreshToken!!)
        Unit
    }
}