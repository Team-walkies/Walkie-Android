package com.startup.domain.repository

import com.startup.domain.model.spot.Spot
import com.startup.domain.model.spot.StartMoveToSpotData
import kotlinx.coroutines.flow.Flow

interface SpotRepository {
    fun searchSpots(): Flow<List<Spot>>
    fun startMoveToSpot(request: StartMoveToSpotData): Flow<Long>
    fun completeMoveToSpot(spotId: Long): Flow<Long>
    fun stopMoveToSpot(): Flow<Long>
    fun getSpotOfVisitor(spotId: Long): Flow<Int>
}