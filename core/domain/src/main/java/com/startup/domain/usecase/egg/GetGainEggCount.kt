package com.startup.domain.usecase.egg

import com.startup.domain.repository.EggRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGainEggCount @Inject constructor(private val eggRepository: EggRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = eggRepository.getMyEggCount()
}