package com.startup.data.remote.datasourceimpl

import androidx.datastore.preferences.core.Preferences
import com.startup.common.util.Printer
import com.startup.common.util.ResponseErrorException
import com.startup.common.util.UserAuthNotFoundException
import com.startup.data.datasource.AuthDataSource
import com.startup.data.local.provider.LogoutManager
import com.startup.data.local.provider.TokenDataStoreProvider
import com.startup.data.remote.dto.request.auth.JoinRequest
import com.startup.data.remote.dto.request.auth.LoginRequest
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.AuthService
import com.startup.data.remote.service.MemberService
import com.startup.data.util.ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.KakaoLogoutHelper
import com.startup.data.util.REFRESH_ACCESS_TOKEN_KEY_NAME
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

internal class AuthDataSourceImpl @Inject constructor(
    private val kakaoLogoutHelper: KakaoLogoutHelper,
    private val authService: AuthService,
    private val memberService: MemberService,
    private val tokenDataStoreProvider: TokenDataStoreProvider,
    private val logoutManager: LogoutManager,
    @Named(ACCESS_TOKEN_KEY_NAME) private val accessTokenKey: Preferences.Key<String>,
    @Named(REFRESH_ACCESS_TOKEN_KEY_NAME) private val refreshTokenKey: Preferences.Key<String>,
) : AuthDataSource {

    override fun login(kakaoToken: String): Flow<Unit> = flow {
        emitRemote(
            authService.login(
                LoginRequest(
                    provider = "kakao",
                    loginAccessToken = kakaoToken
                )
            )
        )
    }.map {
        if (it == null || it.accessToken.isNullOrBlank()) {
            throw UserAuthNotFoundException(
                message = "계정 없음",
                nickName = kakaoLogoutHelper.me().getOrNull()?.kakaoAccount?.profile?.nickname,
                providerToken = kakaoToken
            )
        }
        tokenDataStoreProvider.putValue(accessTokenKey, it.accessToken)
        tokenDataStoreProvider.putValue(refreshTokenKey, it.refreshToken!!)
    }.catch {
        Printer.e("LMH", " EXCEPTION $it")
        if (it is UserAuthNotFoundException || it is ResponseErrorException || it is HttpException) {
            throw it
        } else {
            throw ResponseErrorException("로그인 오류")
        }
    }

    override fun unLink(): Flow<Int> = flow {
        handleExceptionIfNeed {
            emitRemote(memberService.withdrawalService())
            kakaoLogoutHelper.unLink()
            logoutManager.logout()
        }
    }

    override fun logOut(): Flow<Unit> = flow {
        handleExceptionIfNeed {
            kakaoLogoutHelper.logout()
            logoutManager.logout()
            emitRemote(memberService.logoutService())
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
    }

    override fun getAccessToken(): Flow<String> =
        tokenDataStoreProvider.getFlowValue(accessTokenKey)
}