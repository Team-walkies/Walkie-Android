package com.startup.domain.usecase

import com.startup.domain.repository.UserRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTodayStepNotiEnabled @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<Unit, Boolean>() {
    override fun invoke(params: Boolean): Flow<Unit> = flow {
        userRepository.updateNotificationTodayStepEnabled(params)
        emit(Unit)
    }
}