package com.startup.domain.repository

import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HealthcareRepository {
    fun getCalendarHealthcareList(startDate: String, endDate: String): Flow<List<DailyHealthcareListItem>>
    fun getCalendarHealthcareDetail(searchDate: LocalDate): Flow<DailyHealthcareDetail>
    fun getTodayTargetStep(): Flow<Int>
    suspend fun putTodayTargetStep(targetStep: Int)
    fun getCalendarHealthcareContinueDays(): Flow<Int>
}