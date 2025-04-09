package com.startup.domain.usecase

import com.startup.domain.model.egg.UpdateEggStepInfo
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.repository.EggRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateEggOfStepCount @Inject constructor(private val eggRepository: EggRepository) :
    BaseUseCase<UpdateEggStepInfo, UpdateStepData>() {
    override fun invoke(params: UpdateStepData): Flow<UpdateEggStepInfo> = eggRepository.updateEggOfStepCount(params)
}