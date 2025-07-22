package com.startup.data.repository

import com.startup.data.datasource.HealthcareDataSource
import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import com.startup.domain.repository.HealthcareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HealthcareRepositoryImpl @Inject constructor(private val healthcareDataSource: HealthcareDataSource) :
    HealthcareRepository {
    override fun getCalendarHealthcareList(startDate: String, endDate: String): Flow<List<DailyHealthcareListItem>> =
        healthcareDataSource.getCalendarHealthcareList(startDate, endDate)
            .map { list ->
                list.map { item ->
                    item.toDomain()
                }
            }

    override fun getCalendarHealthcareDetail(searchDate: String): Flow<DailyHealthcareDetail> =
        healthcareDataSource.getCalendarHealthcareDetail(searchDate)
            .map { item -> item.toDomain() }
}