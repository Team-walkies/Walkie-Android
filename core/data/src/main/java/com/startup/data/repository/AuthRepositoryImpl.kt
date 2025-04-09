package com.startup.data.repository

import com.startup.data.datasource.AuthDataSource
import com.startup.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    AuthRepository {
    override fun login(): Flow<Unit> =
        authDataSource.login()

    override fun unLink(): Flow<Unit> = authDataSource.unLink()

    override fun logOut(): Flow<Unit> = authDataSource.logOut()
    override fun localLogout(): Flow<Unit> = authDataSource.localLogout()

    override fun joinWalkie(providerToken: String, nickName: String): Flow<Unit> =
        authDataSource.joinWalkie(providerToken, nickName)

    override fun getAccessToken(): Flow<String> = authDataSource.getAccessToken()
}