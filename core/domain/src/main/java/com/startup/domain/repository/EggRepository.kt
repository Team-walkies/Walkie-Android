package com.startup.domain.repository

import com.startup.domain.model.egg.EggDetail
import com.startup.domain.model.egg.MyEgg
import com.startup.domain.model.egg.UpdateStepData
import kotlinx.coroutines.flow.Flow

interface EggRepository {
    fun getEggDetailInfo(eggId: Long): Flow<EggDetail>
    fun updateEggOfStepCount(request: UpdateStepData): Flow<Unit>
    fun getMyEggList(): Flow<List<MyEgg>>
    fun getMyEggCount(): Flow<Int>
}