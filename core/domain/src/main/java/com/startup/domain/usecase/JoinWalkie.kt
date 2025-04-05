package com.startup.domain.usecase

import com.startup.domain.repository.AuthRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinWalkie @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<Unit, Pair<String, String>>() {
    override fun invoke(params: Pair<String, String>): Flow<Unit>{
        val (providerToken, nickName) = params
        return authRepository.joinWalkie(providerToken, nickName)
    }
}