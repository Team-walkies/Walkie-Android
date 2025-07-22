package com.startup.domain.usecase.healthcare

import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.repository.HealthcareRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyHealthcareDetail @Inject constructor(private val healthcareRepository: HealthcareRepository) :
    BaseUseCase<DailyHealthcareDetail, String>() {
    override fun invoke(params: String): Flow<DailyHealthcareDetail> =
        healthcareRepository.getCalendarHealthcareDetail(searchDate = params)
}