package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.HealthcareDataSource
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareDetail
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareListItem
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.HealthcareService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class HealthcareDataSourceImpl @Inject constructor(private val healthcareService: HealthcareService) :
    HealthcareDataSource {
    override fun getCalendarHealthcareList(
        startDate: String,
        endDate: String
    ): Flow<List<ResponseDailyHealthcareListItem>> = flow {
        handleExceptionIfNeed {
            emitRemote(healthcareService.getCalendarHealthcareList(startDate, endDate))
        }
    }

    override fun getCalendarHealthcareDetail(searchDate: String): Flow<ResponseDailyHealthcareDetail> = flow {
        handleExceptionIfNeed {
            emitRemote(healthcareService.getCalendarHealthcareDetail(searchDate))
        }
    }
}