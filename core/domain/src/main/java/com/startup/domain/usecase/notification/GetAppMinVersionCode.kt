package com.startup.domain.usecase.notification

import com.startup.domain.repository.UserRepository
import javax.inject.Inject

class GetAppMinVersionCode @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Long = userRepository.getMinAppVersionCode()
}