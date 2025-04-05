package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun kakaoLogin(): Flow<String>
    fun loginWithKakao(kakaoAccessToken: String): Flow<Unit>
    fun login(): Flow<Unit>
    fun unLink(): Flow<Unit>
    fun logOut(): Flow<Unit>
    fun localLogout(): Flow<Unit>
    fun joinWalkie(providerToken: String, nickName: String): Flow<Unit>
}