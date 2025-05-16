package com.startup.domain.usecase.auth

import com.startup.domain.repository.AuthRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Unlink @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = authRepository.unLink()
}