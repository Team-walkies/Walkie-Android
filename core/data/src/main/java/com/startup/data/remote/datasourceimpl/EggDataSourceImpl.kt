package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.EggDataSource
import com.startup.data.remote.dto.response.egg.EggCountResponse
import com.startup.data.remote.dto.response.egg.EggDetailDto
import com.startup.data.remote.dto.response.egg.MyEggResponse
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.EggService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class EggDataSourceImpl @Inject constructor(private val eggService: EggService) :
    EggDataSource {
    override fun getEggDetailInfo(eggId: Long): Flow<EggDetailDto> = flow {
        handleExceptionIfNeed {
            emitRemote(eggService.getEggDetailInfo(eggId))
        }
    }

    override fun updateEggOfStepCount(request: UpdateEggOfStepCountRequest): Flow<Unit> =
        flow {
            handleExceptionIfNeed {
                emitRemote(eggService.updateEggOfStepCount(request))
            }
        }

    override fun getMyEggList(): Flow<MyEggResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(eggService.getMyEggList())
        }

    }

    override fun getMyEggCount(): Flow<EggCountResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(eggService.getMyEggCount())
        }
    }
}