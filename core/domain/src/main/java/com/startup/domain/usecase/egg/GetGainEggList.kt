package com.startup.domain.usecase.egg

import com.startup.domain.model.egg.MyEgg
import com.startup.domain.repository.EggRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGainEggList @Inject constructor(private val eggRepository: EggRepository) :
    BaseUseCase<List<MyEgg>, Unit>() {
    override fun invoke(params: Unit): Flow<List<MyEgg>> = eggRepository.getMyEggList()
}