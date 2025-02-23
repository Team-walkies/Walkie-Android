package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.data.datasource.SpotDataSource
import com.startup.data.remote.dto.request.spot.StartMoveToSpotRequest
import com.startup.domain.model.spot.Spot
import com.startup.domain.model.spot.StartMoveToSpotData
import com.startup.domain.repository.SpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SpotRepositoryImpl @Inject constructor(private val spotDataSource: SpotDataSource) :
    SpotRepository {
    override fun searchSpots(): Flow<List<Spot>> =
        spotDataSource.searchSpots().map { it.spots?.map { it.toDomain() } ?: emptyList() }

    override fun startMoveToSpot(request: StartMoveToSpotData): Flow<Long> =
        spotDataSource.startMoveToSpot(
            StartMoveToSpotRequest(
                eggId = request.eggId,
                characterId = request.characterId,
                startLongitude = request.startLongitude,
                startLatitude = request.startLatitude,
                startTime = request.startTime,
                spotId = request.spotId,
                memberId = request.memberId
            )
        ).map { it.curSpotId.orZero() }

    override fun completeMoveToSpot(spotId: Long): Flow<Long> =
        spotDataSource.completeMoveToSpot(spotId).map { it.curSpotId.orZero() }

    override fun stopMoveToSpot(): Flow<Long> =
        spotDataSource.stopMoveToSpot().map { it.curSpotId.orZero() }

    override fun getSpotOfVisitor(spotId: Long): Flow<Int> =
        spotDataSource.getSpotOfVisitor(spotId).map { it.curVisitant.orZero() }
}