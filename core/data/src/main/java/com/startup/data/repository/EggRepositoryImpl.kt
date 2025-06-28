package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.data.datasource.EggDataSource
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import com.startup.domain.model.egg.DailyEgg
import com.startup.domain.model.egg.EggDetail
import com.startup.domain.model.egg.MyEgg
import com.startup.domain.model.egg.UpdateEggStepInfo
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.EggRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class EggRepositoryImpl @Inject constructor(
    private val eggDataSource: EggDataSource,
    private val stepDataStore: StepDataStore
) :
    EggRepository {
    override fun getEggDetailInfo(eggId: Long): Flow<EggDetail> =
        eggDataSource.getEggDetailInfo(eggId).map { it.toDomain(eggId) }

    override fun updateEggOfStepCount(request: UpdateStepData): Flow<UpdateEggStepInfo> =
        eggDataSource.updateEggOfStepCount(
            UpdateEggOfStepCountRequest(
                eggId = request.eggId,
                nowStep = request.nowStep,
                longitude = request.longitude,
                latitude = request.latitude
            )
        ).map { it.toDomain() }

    override fun getMyEggList(): Flow<List<MyEgg>> =
        eggDataSource.getMyEggList().map { response ->
            val currentWalkEggId = stepDataStore.getCurrentWalkEggId()
            response.eggs?.map { egg ->
                val convertEgg = egg.toDomain()
                if (convertEgg.play && convertEgg.eggId == currentWalkEggId) {
                    convertEgg.copy(nowStep = stepDataStore.getEggCurrentSteps())
                } else {
                    convertEgg
                }
            } ?: emptyList()
        }

    override fun getMyEggCount(): Flow<Int> =
        eggDataSource.getMyEggCount().map { it.eggCount.orZero() }

    override fun getDailyEgg(): Flow<DailyEgg> =
        eggDataSource.getDailyEgg().map { it.toDomain() }
}