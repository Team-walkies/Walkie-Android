package com.startup.domain.usecase.auth

import com.startup.domain.repository.AuthRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinWalkie @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<Unit, Pair<String, String>>() {
    override fun invoke(params: Pair<String, String>): Flow<Unit>{
        val (providerToken, nickName) = params
        return authRepository.joinWalkie(providerToken, nickName)
    }
}