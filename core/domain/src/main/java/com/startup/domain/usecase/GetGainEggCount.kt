package com.startup.domain.usecase

import com.startup.domain.repository.EggRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGainEggCount @Inject constructor(private val eggRepository: EggRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = eggRepository.getMyEggCount()
}