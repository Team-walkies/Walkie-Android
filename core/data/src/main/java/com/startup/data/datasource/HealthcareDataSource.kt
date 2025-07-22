package com.startup.data.datasource

import com.startup.data.remote.dto.response.healthcare.ResponseContinuousDays
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareDetail
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareListItem
import kotlinx.coroutines.flow.Flow

interface HealthcareDataSource {
    fun getCalendarHealthcareList(startDate: String, endDate: String): Flow<List<ResponseDailyHealthcareListItem>>
    fun getCalendarHealthcareDetail(searchDate: String): Flow<ResponseDailyHealthcareDetail>
    fun getCalendarHealthcareContinueDays(): Flow<ResponseContinuousDays>
}