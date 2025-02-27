package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.AuthDataSource
import com.startup.data.util.KaKaoLoginClient
import com.startup.data.util.KakaoAuthFailException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class AuthDataSourceImpl @Inject constructor(private val kaKaoLoginClient: KaKaoLoginClient) : // TODO DataSourceModule 에 Inject 추가
    AuthDataSource {
    override suspend fun kakaoLogin(): Flow<String> = flow {
        kaKaoLoginClient.login().fold(
            onSuccess = { token ->
                emit(token.accessToken)
            },
            onFailure = { error ->
                throw KakaoAuthFailException("로그인 오류 : $error")
            }
        )
    }

    override suspend fun login(): Flow<Unit> = flow {
        // TODO AuthService 와 연동
    }

    override suspend fun unLink(): Flow<Unit> = flow {
        // TODO AuthService 와 연동
        kaKaoLoginClient.unLink()
    }

    override suspend fun logOut(): Flow<Unit> = flow {
        // TODO AuthService 와 연동
        kaKaoLoginClient.logout()
    }
}