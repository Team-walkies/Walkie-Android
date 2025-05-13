package com.startup.domain.usecase.notification

import com.startup.domain.repository.UserRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateArriveSpotNotiEnabled @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<Unit, Boolean>() {
    override fun invoke(params: Boolean): Flow<Unit> = flow<Unit> {
        userRepository.updateNotificationSpotArriveEnabled(params)
        emit(Unit)
    }
}