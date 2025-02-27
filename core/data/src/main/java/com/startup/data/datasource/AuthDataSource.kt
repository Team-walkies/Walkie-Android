package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun kakaoLogin(): Flow<String>
    suspend fun login(): Flow<Unit>
    suspend fun unLink(): Flow<Unit>
    suspend fun logOut(): Flow<Unit>
}