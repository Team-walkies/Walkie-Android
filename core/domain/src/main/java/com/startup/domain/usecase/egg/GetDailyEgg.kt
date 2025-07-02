package com.startup.domain.usecase.egg

import com.startup.domain.model.egg.DailyEgg
import com.startup.domain.repository.EggRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyEgg @Inject constructor(private val eggRepository: EggRepository) :
    BaseUseCase<DailyEgg, Unit>() {
    override fun invoke(params: Unit): Flow<DailyEgg> = eggRepository.getDailyEgg()
}