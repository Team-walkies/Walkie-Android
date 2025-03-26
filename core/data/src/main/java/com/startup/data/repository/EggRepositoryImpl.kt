package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.data.datasource.EggDataSource
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import com.startup.domain.model.egg.EggDetail
import com.startup.domain.model.egg.MyEgg
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.repository.EggRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class EggRepositoryImpl @Inject constructor(private val eggDataSource: EggDataSource) :
    EggRepository {
    override fun getEggDetailInfo(eggId: Long): Flow<EggDetail> =
        eggDataSource.getEggDetailInfo(eggId).map { it.toDomain(eggId) }

    override fun updateEggOfStepCount(request: UpdateStepData): Flow<Unit> =
        eggDataSource.updateEggOfStepCount(
            UpdateEggOfStepCountRequest(
                eggId = request.eggId,
                nowStep = request.nowStep,
                longitude = request.longitude,
                latitude = request.latitude
            )
        )

    override fun getMyEggList(): Flow<List<MyEgg>> =
        eggDataSource.getMyEggList().map { it.eggs?.map { it.toDomain() } ?: emptyList() }

    override fun getMyEggCount(): Flow<Int> =
        eggDataSource.getMyEggCount().map { it.eggCount.orZero() }
}