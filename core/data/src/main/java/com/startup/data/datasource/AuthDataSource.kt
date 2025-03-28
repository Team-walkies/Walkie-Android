package com.startup.data.datasource

import com.startup.data.remote.dto.response.auth.TokenDto
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun kakaoLogin(): Flow<String>
    fun login(): Flow<Unit>
    fun unLink(): Flow<Unit>
    fun logOut(): Flow<Unit>
    fun join(providerToken: String, nickName:String): Flow<Unit>
}