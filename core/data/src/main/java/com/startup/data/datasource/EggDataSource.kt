package com.startup.data.datasource

import com.startup.data.remote.dto.response.egg.DailyEggResponse
import com.startup.data.remote.dto.response.egg.EggCountResponse
import com.startup.data.remote.dto.response.egg.EggDetailDto
import com.startup.data.remote.dto.response.egg.MyEggResponse
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import com.startup.data.remote.dto.response.egg.EggStepUpdateResponse
import kotlinx.coroutines.flow.Flow

interface EggDataSource {
    fun getEggDetailInfo(eggId: Long): Flow<EggDetailDto>
    fun updateEggOfStepCount(request: UpdateEggOfStepCountRequest): Flow<EggStepUpdateResponse>
    fun getMyEggList(): Flow<MyEggResponse>
    fun getMyEggCount(): Flow<EggCountResponse>
    fun getDailyEgg(): Flow<DailyEggResponse>
}