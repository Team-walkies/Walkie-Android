package com.startup.domain.usecase

import com.startup.domain.repository.AuthRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWalkie @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<Unit, String>() {
    override fun invoke(params: String): Flow<Unit> =
        authRepository.login(params)
}