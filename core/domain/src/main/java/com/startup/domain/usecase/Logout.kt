package com.startup.domain.usecase

import com.startup.domain.repository.AuthRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Logout @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<Unit, Unit>() {
    override fun invoke(params: Unit): Flow<Unit> = authRepository.logOut()
}