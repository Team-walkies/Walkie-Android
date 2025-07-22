package com.startup.domain.usecase.healthcare

import com.startup.domain.repository.HealthcareRepository
import javax.inject.Inject

class PutTargetStep @Inject constructor(private val healthcareRepository: HealthcareRepository) {
    suspend fun invoke(params: Int) {
        healthcareRepository.putTodayTargetStep(params)
    }
}