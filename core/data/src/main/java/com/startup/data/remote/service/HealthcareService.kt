package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.healthcare.ResponseContinuousDays
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareDetail
import com.startup.data.remote.dto.response.healthcare.ResponseDailyHealthcareListItem
import retrofit2.http.GET
import retrofit2.http.Query

internal interface HealthcareService {

    @GET("api/v1/health")
    suspend fun getCalendarHealthcareList(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): BaseResponse<List<ResponseDailyHealthcareListItem>>

    @GET("api/v1/health/detail")
    suspend fun getCalendarHealthcareDetail(@Query("searchDate") searchDate: String): BaseResponse<ResponseDailyHealthcareDetail>

    /** 어제까지의 헬스케어 연속 달성 일수 */
    @GET("api/v1/health/continueDays")
    suspend fun getCalendarHealthcareContinueDays(): BaseResponse<ResponseContinuousDays>
}