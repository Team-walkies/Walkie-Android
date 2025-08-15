package com.startup.domain.usecase.healthcare

import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.repository.HealthcareRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDailyHealthcareDetail @Inject constructor(private val healthcareRepository: HealthcareRepository) :
    BaseUseCase<DailyHealthcareDetail, LocalDate>() {
    override fun invoke(params: LocalDate): Flow<DailyHealthcareDetail> =
        healthcareRepository.getCalendarHealthcareDetail(searchDate = params)
}