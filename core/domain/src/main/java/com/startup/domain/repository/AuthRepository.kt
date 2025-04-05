package com.startup.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(): Flow<Unit>
    fun unLink(): Flow<Unit>
    fun logOut(): Flow<Unit>
    fun localLogout(): Flow<Unit>
    fun joinWalkie(providerToken: String, nickName:String): Flow<Unit>
}