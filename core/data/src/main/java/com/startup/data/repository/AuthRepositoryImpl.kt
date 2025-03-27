package com.startup.data.repository

import com.startup.data.datasource.AuthDataSource
import com.startup.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    AuthRepository {
    override fun login(): Flow<Unit> = authDataSource.login()

    override fun unLink(): Flow<Unit> = authDataSource.unLink()

    override fun logOut(): Flow<Unit> = authDataSource.logOut()
    override fun join(providerToken: String, nickName: String): Flow<Unit> =
        authDataSource.join(providerToken, nickName)
}