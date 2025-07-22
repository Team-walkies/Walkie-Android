package com.startup.domain.repository

import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import kotlinx.coroutines.flow.Flow

interface HealthcareRepository {
    fun getCalendarHealthcareList(startDate: String, endDate: String): Flow<List<DailyHealthcareListItem>>
    fun getCalendarHealthcareDetail(searchDate: String): Flow<DailyHealthcareDetail>
}