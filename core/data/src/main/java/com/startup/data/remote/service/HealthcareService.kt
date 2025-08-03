package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.healthcare.PutTodayWalkRequest
import com.startup.data.remote.dto.response.healthcare.PutTodayWalkDto
import retrofit2.http.Body
import retrofit2.http.PUT

interface HealthcareService {
    @PUT("api/v1/health")
    suspend fun putHealthcareInfo(@Body request: PutTodayWalkRequest): BaseResponse<PutTodayWalkDto>
}