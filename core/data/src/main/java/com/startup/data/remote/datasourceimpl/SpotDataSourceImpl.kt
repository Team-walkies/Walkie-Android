package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.SpotDataSource
import com.startup.data.remote.dto.response.spot.CurrentSpotIdResponse
import com.startup.data.remote.dto.response.spot.CurrentVisitantResponse
import com.startup.data.remote.dto.response.spot.SpotInfoResponse
import com.startup.data.remote.dto.request.spot.StartMoveToSpotRequest
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.SpotService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class SpotDataSourceImpl @Inject constructor(private val spotService: SpotService): SpotDataSource {
    override fun searchSpots(): Flow<SpotInfoResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(spotService.searchSpots())
        }
    }

    override fun startMoveToSpot(request: StartMoveToSpotRequest): Flow<CurrentSpotIdResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(spotService.startMoveToSpot(request))
        }
    }

    override fun completeMoveToSpot(spotId: Long): Flow<CurrentSpotIdResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(spotService.completeMoveToSpot(spotId))
        }
    }

    override fun stopMoveToSpot(): Flow<CurrentSpotIdResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(spotService.stopMoveToSpot())
        }
    }

    override fun getSpotOfVisitor(spotId: Long): Flow<CurrentVisitantResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(spotService.getSpotOfVisitor(spotId))
        }
    }
}