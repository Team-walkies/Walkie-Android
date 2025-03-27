package com.startup.domain.usecase

import com.startup.domain.repository.UserRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayStepNotiEnabled @Inject constructor(private val userRepository: UserRepository): BaseUseCase<Boolean, Unit>() {
    override fun invoke(params: Unit): Flow<Boolean> = userRepository.getNotificationTodayStepEnabled()
}