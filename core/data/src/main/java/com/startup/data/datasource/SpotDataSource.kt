package com.startup.data.datasource

import com.startup.data.remote.dto.response.spot.CurrentSpotIdResponse
import com.startup.data.remote.dto.response.spot.CurrentVisitantResponse
import com.startup.data.remote.dto.response.spot.SpotInfoResponse
import com.startup.data.remote.dto.request.spot.StartMoveToSpotRequest
import kotlinx.coroutines.flow.Flow

interface SpotDataSource {
    fun searchSpots(): Flow<SpotInfoResponse>
    fun startMoveToSpot(request: StartMoveToSpotRequest): Flow<CurrentSpotIdResponse>
    fun completeMoveToSpot(spotId: Long): Flow<CurrentSpotIdResponse>
    fun stopMoveToSpot(): Flow<CurrentSpotIdResponse>
    fun getSpotOfVisitor(spotId: Long): Flow<CurrentVisitantResponse>
}