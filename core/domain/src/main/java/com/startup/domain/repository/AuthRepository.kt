package com.startup.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(kakaoToken: String): Flow<Unit>
    fun unLink(): Flow<Int>
    fun logOut(): Flow<Unit>
    fun localLogout(): Flow<Unit>
    fun joinWalkie(providerToken: String, nickName:String): Flow<Unit>
    fun getAccessToken(): Flow<String>
}