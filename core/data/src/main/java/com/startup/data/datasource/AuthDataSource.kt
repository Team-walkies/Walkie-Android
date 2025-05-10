package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun login(kakaoToken: String): Flow<Unit>
    fun unLink(): Flow<Int>
    fun logOut(): Flow<Unit>
    fun localLogout(): Flow<Unit>
    fun joinWalkie(providerToken: String, nickName: String): Flow<Unit>
    fun getAccessToken(): Flow<String>
}