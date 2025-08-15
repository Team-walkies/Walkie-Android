package com.startup.domain.usecase.healthcare

import com.startup.domain.model.healthcare.DailyHealthcareListItem
import com.startup.domain.repository.HealthcareRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRangeOfWeekDailyHealthcareList @Inject constructor(private val healthcareRepository: HealthcareRepository) :
    BaseUseCase<List<DailyHealthcareListItem>, Pair<String, String>>() {
    override fun invoke(params: Pair<String, String>): Flow<List<DailyHealthcareListItem>> =
        healthcareRepository.getCalendarHealthcareList(startDate = params.first, endDate = params.second)
}