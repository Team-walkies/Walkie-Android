package com.startup.domain.usecase.profile

import com.startup.domain.repository.UserRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileAccessEnabled @Inject constructor(private val userRepository: UserRepository): BaseUseCase<Boolean, Unit>() {
    override fun invoke(params: Unit): Flow<Boolean> = userRepository.getProfileAccessEnabled()
}