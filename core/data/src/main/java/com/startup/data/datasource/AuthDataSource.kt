package com.startup.data.datasource

import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun kakaoLogin(): Flow<String>
    fun login(): Flow<Unit>
    fun unLink(): Flow<Unit>
    fun logOut(): Flow<Unit>
}