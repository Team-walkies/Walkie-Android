package com.startup.domain.usecase.healthcare

import com.startup.domain.repository.HealthcareRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTargetStep @Inject constructor(private val healthcareRepository: HealthcareRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = healthcareRepository.getTodayTargetStep()
}